package com.abatts.inboundbot.command.commands.playerwarps;

import com.abatts.inboundbot.Bot;
import com.abatts.inboundbot.command.Command;
import com.abatts.inboundbot.database.SQLDatabaseConnection;
import com.abatts.inboundbot.permission.PermissionManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DeleteWarpCommand implements Command {
    @Override
    public void runCommand(GuildMessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();
        String ownerId;
        String[] args = message.split(" ");

        if (args.length < 3){
            EmbedBuilder embed = new EmbedBuilder()
                    .setAuthor("Please specify the name of the player warp you want to delete", null, event.getJDA().getSelfUser().getAvatarUrl())
                    .setColor(Color.red);
            event.getMessage().getChannel().sendMessageEmbeds(embed.build()).queue();
            return;
        }

        try (PreparedStatement statement = Bot.connection.prepareStatement("""
                SELECT * FROM PLAYER_WARPS WHERE name = ? AND guild_id = ?
            """)) {
            statement.setString(1, args[2]);
            statement.setString(2, event.getGuild().getId());
            ResultSet rs = statement.executeQuery();
            if (! rs.next()){
                EmbedBuilder embed = new EmbedBuilder()
                        .setAuthor("I couldn't find a player warp in this server with that name", null, event.getJDA().getSelfUser().getAvatarUrl())
                        .setColor(Color.red);
                event.getMessage().getChannel().sendMessageEmbeds(embed.build()).queue();
                return;
            }
            ownerId = rs.getString("owner_id");
            if (PermissionManager.isAdmin(event.getMember()) || ownerId.equals(event.getAuthor().getId())){
                PreparedStatement statement1 = Bot.connection.prepareStatement("""
                    DELETE FROM PLAYER_WARPS WHERE owner_id = ? AND guild_id = ? AND name = ?
                """);
                statement1.setString(1, event.getAuthor().getId());
                statement1.setString(2, event.getGuild().getId());
                statement1.setString(3, args[2]);
                statement1.executeUpdate();
                EmbedBuilder embed = new EmbedBuilder()
                        .setAuthor("Successfully deleted that player warp!", null, event.getJDA().getSelfUser().getAvatarUrl())
                        .setColor(Color.green);
                event.getMessage().getChannel().sendMessageEmbeds(embed.build()).queue();
            }
            else{
                EmbedBuilder embed = new EmbedBuilder()
                        .setAuthor("You can't delete a warp you don't own!", null, event.getJDA().getSelfUser().getAvatarUrl())
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
        return "pw delete";
    }

    @Override
    public String getDescription() {
        return "Delete a warp from the player warps list";
    }
}
