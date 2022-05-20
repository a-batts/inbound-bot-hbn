package com.abatts.inboundbot.command.commands.playerwarps;

import com.abatts.inboundbot.Bot;
import com.abatts.inboundbot.command.Command;
import com.abatts.inboundbot.permission.PermissionManager;
import com.abatts.inboundbot.util.PlayerWarpHelper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EditWarpCommand implements Command {
    @Override
    public void runCommand(GuildMessageReceivedEvent event) {
        String description = "";
        String message = event.getMessage().getContentRaw();
        String ownerId;
        String[] args = message.split(" ");

        if (args.length < 3){
            EmbedBuilder embed = new EmbedBuilder()
                    .setAuthor("Please specify the name of the player warp you want to edit", null, event.getJDA().getSelfUser().getAvatarUrl())
                    .setColor(Color.red);
            event.getMessage().getChannel().sendMessageEmbeds(embed.build()).queue();
            return;
        }

        try (ResultSet rs = PlayerWarpHelper.dbQueryWarp(args[2], event.getGuild().getId())) {
            if (! rs.next())
                PlayerWarpHelper.throwWarpDoesNotExists(event);
            ownerId = rs.getString("owner_id");
            if (PermissionManager.isAdmin(event.getMember()) || ownerId.equals(event.getAuthor().getId())){
                if (args.length >= 4)
                    description = message.substring(message.indexOf(args[3]));

                PreparedStatement statement1 = Bot.connection.prepareStatement("""
                    UPDATE PLAYER_WARPS
                    SET description = ?
                    WHERE owner_id = ? AND guild_id = ? AND name = ?
                """);
                statement1.setString(1, description);
                statement1.setString(2, event.getAuthor().getId());
                statement1.setString(3, event.getGuild().getId());
                statement1.setString(4, args[2]);
                statement1.executeUpdate();
                EmbedBuilder embed = new EmbedBuilder()
                        .setAuthor("Successfully updated the description for that player warp!", null, event.getJDA().getSelfUser().getAvatarUrl())
                        .setColor(Color.green);
                event.getMessage().getChannel().sendMessageEmbeds(embed.build()).queue();
            }
            else{
                EmbedBuilder embed = new EmbedBuilder()
                        .setAuthor("You can't edit a warp you don't own!", null, event.getJDA().getSelfUser().getAvatarUrl())
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
        return "pw edit";
    }

    @Override
    public String getDescription() {
        return "Edit the description of a specified warp";
    }
}
