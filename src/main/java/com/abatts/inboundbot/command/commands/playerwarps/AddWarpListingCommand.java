package com.abatts.inboundbot.command.commands.playerwarps;

import com.abatts.inboundbot.Bot;
import com.abatts.inboundbot.command.Command;
import com.abatts.inboundbot.util.PlayerWarpHelper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddWarpListingCommand implements Command {
    @Override
    public void runCommand(GuildMessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();
        String ownerId;
        String[] args = message.split(" ");

        if (args.length < 4){
            EmbedBuilder embed = new EmbedBuilder()
                    .setAuthor("Please use the command format - pw list <pw name> <item to list>", null, event.getJDA().getSelfUser().getAvatarUrl())
                    .setColor(Color.red);
            event.getMessage().getChannel().sendMessageEmbeds(embed.build()).queue();
            return;
        }

        try (ResultSet rs = PlayerWarpHelper.dbQueryWarp(args[2], event.getGuild().getId())) {
            if (! rs.next()) {
                PlayerWarpHelper.throwWarpDoesNotExists(event);
                return;
            }
            ownerId = rs.getString("owner_id");

            String[] items = message.substring(message.indexOf(args[3])).split(", ");

            if (ownerId.equals(event.getAuthor().getId())) {
                for (String item: items){
                    try(PreparedStatement statement = Bot.getConnection().prepareStatement("""
                        INSERT INTO WARP_LISTINGS(warp_id, name)
                        VALUES (?, ?)
                    """)){
                        statement.setInt(1, rs.getInt("id"));
                        statement.setString(2, item);
                        statement.executeUpdate();
                    }
                    catch (SQLException e){
                        e.printStackTrace();
                    }
                }
                EmbedBuilder embed = new EmbedBuilder()
                        .setAuthor("Successfully listed the item(s)", null, event.getJDA().getSelfUser().getAvatarUrl())
                        .setColor(Color.green);
                event.getMessage().getChannel().sendMessageEmbeds(embed.build()).queue();
            }
            else{
                EmbedBuilder embed = new EmbedBuilder()
                        .setAuthor("You can't list an item on a warp you don't own!", null, event.getJDA().getSelfUser().getAvatarUrl())
                        .setColor(Color.red);
                event.getMessage().getChannel().sendMessageEmbeds(embed.build()).queue();
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return "pw list";
    }

    @Override
    public String getDescription() {
        return "List an item on a specific player warp \n`pw list <pw name> <item1, item2, item3,...>`";
    }
}
