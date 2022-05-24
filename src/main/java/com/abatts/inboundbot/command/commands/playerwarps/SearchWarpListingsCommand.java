package com.abatts.inboundbot.command.commands.playerwarps;

import com.abatts.inboundbot.Bot;
import com.abatts.inboundbot.command.Command;
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
import java.util.concurrent.TimeUnit;

public class SearchWarpListingsCommand implements Command {

    private final Paginator.Builder warplistEmbed = new Paginator.Builder()
            .setColumns(1)
            .setFinalAction(m -> {try{m.clearReactions().queue();}catch(PermissionException ignore){}})
            .setItemsPerPage(10)
            .waitOnSinglePage(false)
            .showPageNumbers(true)
            .wrapPageEnds(true)
            .setEventWaiter(Bot.getEventWaiter())
            .setTimeout(1, TimeUnit.MINUTES);
    public SearchWarpListingsCommand(){
        try(PreparedStatement statement = Bot.getConnection().prepareStatement("""
            CREATE TABLE IF NOT EXISTS WARP_LISTINGS
            (
                warp_id INT(20) NOT NULL,
                name VARCHAR(50) NOT NULL
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
        String message = event.getMessage().getContentRaw();

        String[] args = event.getMessage().getContentRaw().split(" ");
        if (args.length < 3){
            EmbedBuilder embed = new EmbedBuilder()
                    .setAuthor("Please specify the item you are searching for", null, event.getJDA().getSelfUser().getAvatarUrl())
                    .setColor(Color.red);
            event.getMessage().getChannel().sendMessageEmbeds(embed.build()).queue();
            return;
        }
        try(PreparedStatement statement = Bot.getConnection().prepareStatement("""
            SELECT *
            FROM WARP_LISTINGS
            WHERE name LIKE ?
        """)){
            statement.setString(1, "%" + message.substring(message.indexOf(args[2])));
            ResultSet rs = statement.executeQuery();

            ArrayList<Integer> pwList = new ArrayList<>();
            while (rs.next()){
                if (! pwList.contains(rs.getInt("warp_id")))
                    pwList.add(rs.getInt("warp_id"));
            }
            if (pwList.isEmpty()){
                EmbedBuilder embed = new EmbedBuilder()
                        .setAuthor("No player warps have listed that item", null, event.getJDA().getSelfUser().getAvatarUrl());
                event.getMessage().getChannel().sendMessageEmbeds(embed.build()).queue();
                return;
            }

            try (PreparedStatement statement1 = Bot.getConnection().prepareStatement("""
                SELECT *
                FROM PLAYER_WARPS
            """)) {
                statement1.setString(1, event.getGuild().getId());
                ResultSet ws = statement1.executeQuery();

                String[] warpsListing = new String[pwList.size()];
                for (int i = 0; i < pwList.size(); i++){
                    System.out.println(pwList.get(i));
                    ws.absolute(pwList.get(i));
                    User user = event.getJDA().retrieveUserById(ws.getString("owner_id")).complete();
                    warpsListing[i] = "pw " + ws.getString("name") + " - Owned by: " + user.getAsMention();
                }
                warplistEmbed.setText("Player warps with " + message.substring(message.indexOf(args[2])) + " in **" + event.getGuild().getName() + "**: ").setItems(warpsListing);
                warplistEmbed.build().display(event.getMessage().getChannel());
            }
            catch(SQLException e){
                e.printStackTrace();
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return "pw search";
    }

    @Override
    public String getDescription() {
        return "Search player warp listings for a specified item \n`pw search <item name>`";
    }
}
