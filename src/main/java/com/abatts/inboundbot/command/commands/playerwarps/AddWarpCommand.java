package com.abatts.inboundbot.command.commands.playerwarps;

import com.abatts.inboundbot.Bot;
import com.abatts.inboundbot.command.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddWarpCommand implements Command {

    private final String NATION_MEMBER_ROLE = "nation member";

    @Override
    public void runCommand(GuildMessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();
        Role nationMemberRole = null;

        for (Role r: event.getGuild().getRoles()){
            if (r.getName().toLowerCase().startsWith(NATION_MEMBER_ROLE))
                nationMemberRole = r;
        }

        if (nationMemberRole != null && event.getMessage().getMember().getRoles().contains(nationMemberRole)){
            String description = "";
            String[] args = message.split(" ");
            if (args.length < 3){
                EmbedBuilder embed = new EmbedBuilder()
                        .setAuthor("Please follow the command format: pw add <warp name> <optional description>", null, event.getJDA().getSelfUser().getAvatarUrl())
                        .setColor(Color.red);
                event.getMessage().getChannel().sendMessageEmbeds(embed.build()).queue();
                return;
            }

            if (args.length >= 4)
                description = String.join(" ", event.getMessage().getContentRaw().substring(message.indexOf(args[3])));

            try (PreparedStatement statement = Bot.connection.prepareStatement("""
                SELECT 1 FROM PLAYER_WARPS WHERE name = ? AND guild_id = ?
            """)) {
                statement.setString(1, args[2]);
                statement.setString(2, event.getGuild().getId());
                ResultSet rs = statement.executeQuery();
                if (rs.next()){
                    EmbedBuilder embed = new EmbedBuilder()
                            .setAuthor("A pw with that name already exists", null, event.getJDA().getSelfUser().getAvatarUrl())
                            .setColor(Color.red);
                    event.getMessage().getChannel().sendMessageEmbeds(embed.build()).queue();
                    return;
                }
            }
            catch (SQLException e){
                e.printStackTrace();
            }

            try (PreparedStatement statement = Bot.connection.prepareStatement("""
                INSERT INTO PLAYER_WARPS(owner_id, guild_id, name, description)
                VALUES (?, ?, ?, ?)
            """)) {
                statement.setString(1, event.getAuthor().getId());
                statement.setString(2, event.getGuild().getId());
                statement.setString(3, args[2]);
                statement.setString(4, description);
                statement.executeUpdate();

                EmbedBuilder embed = new EmbedBuilder()
                        .setAuthor("Successfully added the warp", null, event.getJDA().getSelfUser().getAvatarUrl())
                        .setColor(Color.green);
                event.getMessage().getChannel().sendMessageEmbeds(embed.build()).queue();
            }
            catch(SQLException e){
                e.printStackTrace();
            }
        }
        else{
            EmbedBuilder embed = new EmbedBuilder()
                    .setAuthor("You need be a nation member to add your player warp", null, event.getJDA().getSelfUser().getAvatarUrl())
                    .setColor(Color.red);
            event.getMessage().getChannel().sendMessageEmbeds(embed.build()).queue();
        }
    }

    @Override
    public String getName() {
        return "pw add";
    }

    @Override
    public String getDescription() {
        return "Add a warp to the nation player warps list (format: pw add <warp name> <optional description>)";
    }
}
