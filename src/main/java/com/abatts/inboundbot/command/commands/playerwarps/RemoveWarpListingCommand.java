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

public class RemoveWarpListingCommand implements Command {
    @Override
    public void runCommand(GuildMessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();
        String ownerId;
        String[] args = message.split(" ");

        if (args.length < 4){
            EmbedBuilder embed = new EmbedBuilder()
                    .setAuthor("Please use the command format - pw list <pw name> <items to unlist>", null, event.getJDA().getSelfUser().getAvatarUrl())
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
                    try(PreparedStatement statement = Bot.connection.prepareStatement("""
                        DELETE FROM WARP_LISTINGS WHERE warp_id = ? AND name = ?
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
                        .setAuthor("Successfully delisted the item(s)", null, event.getJDA().getSelfUser().getAvatarUrl())
                        .setColor(Color.green);
                event.getMessage().getChannel().sendMessageEmbeds(embed.build()).queue();
            }
            else{
                EmbedBuilder embed = new EmbedBuilder()
                        .setAuthor("You can't delist an item on a warp you don't own!", null, event.getJDA().getSelfUser().getAvatarUrl())
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
        return "pw unlist";
    }

    @Override
    public String getDescription() {
        return "Unlist an item on a specific player warp \n`pw unlist <pw name> <item1, item2, item3,...>`";
    }
}
