package com.abatts.inboundbot.command.commands.playerwarps;

import com.abatts.inboundbot.Bot;
import com.abatts.inboundbot.command.Command;
import com.abatts.inboundbot.database.SQLDatabaseConnection;
import com.jagrosh.jdautilities.menu.Paginator;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.PermissionException;

import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class ListWarpCommand implements Command {
    private final Paginator.Builder warplistEmbed = new Paginator.Builder()
            .setColumns(1)
            .setFinalAction(m -> {try{m.clearReactions().queue();}catch(PermissionException ignore){}})
            .setItemsPerPage(10)
            .waitOnSinglePage(false)
            .useNumberedItems(true)
            .showPageNumbers(true)
            .wrapPageEnds(true)
            .setEventWaiter(Bot.getEventWaiter())
            .setTimeout(1, TimeUnit.MINUTES);
    public ListWarpCommand(){
        try(PreparedStatement statement = SQLDatabaseConnection.getConnection().prepareStatement("""
            CREATE TABLE IF NOT EXISTS PLAYER_WARPS
            (
                owner_id VARCHAR(20) NOT NULL,
                guild_id VARCHAR(20) NOT NULL,
                name VARCHAR(50) NOT NULL,
                description VARCHAR(255) NOT NULL
            )
            """)){
        statement.execute();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void runCommand(GuildMessageReceivedEvent event) {
        try (PreparedStatement statement = SQLDatabaseConnection.getConnection().prepareStatement("""
            SELECT *
            FROM PLAYER_WARPS
            WHERE guild_id = ?
        """)) {
            statement.setString(1, event.getGuild().getId());
            ResultSet rs = statement.executeQuery();

            ArrayList<String> playerWarps = new ArrayList<>();

            while (rs.next()) {
                System.out.println(rs.getString("owner_id"));
                User user = event.getJDA().retrieveUserById(rs.getString("owner_id")).complete();
                playerWarps.add(rs.getString("name") + " - " + rs.getString("description") + "\n Added by: " + user.getAsMention());
            }

            Collections.sort(playerWarps);

            if (playerWarps.isEmpty()){
                EmbedBuilder embed = new EmbedBuilder()
                        .setAuthor("I couldn't find any player warps for this server", null, event.getJDA().getSelfUser().getAvatarUrl());
                event.getMessage().getChannel().sendMessageEmbeds(embed.build()).queue();
                return;
            }

            warplistEmbed.setText("Player warps for `" + event.getGuild().getName() + "`: ").setItems(playerWarps.toArray(new String[0]));
            warplistEmbed.build().display(event.getMessage().getChannel());
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return "pw list";
    }

    @Override
    public String getDescription() {
        return "View a list of nation player warps";
    }
}
