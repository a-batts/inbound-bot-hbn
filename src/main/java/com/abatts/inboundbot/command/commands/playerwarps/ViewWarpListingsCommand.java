package com.abatts.inboundbot.command.commands.playerwarps;

import com.abatts.inboundbot.Bot;
import com.abatts.inboundbot.command.Command;
import com.abatts.inboundbot.util.PlayerWarpHelper;
import com.jagrosh.jdautilities.menu.Paginator;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.PermissionException;

import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class ViewWarpListingsCommand implements Command {

    private final Paginator.Builder warplistEmbed = new Paginator.Builder()
            .setColumns(1)
            .setFinalAction(m -> {try{m.clearReactions().queue();}catch(PermissionException ignore){}})
            .setItemsPerPage(15)
            .waitOnSinglePage(false)
            .showPageNumbers(true)
            .wrapPageEnds(true)
            .setEventWaiter(Bot.getEventWaiter())
            .setTimeout(1, TimeUnit.MINUTES);

    @Override
    public void runCommand(GuildMessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();
        String[] args = message.split(" ");

        if (args.length < 3){
            EmbedBuilder embed = new EmbedBuilder()
                    .setAuthor("Please specify the player warp to view listings for", null, event.getJDA().getSelfUser().getAvatarUrl())
                    .setColor(Color.red);
            event.getMessage().getChannel().sendMessageEmbeds(embed.build()).queue();
            return;
        }

        try (ResultSet rs = PlayerWarpHelper.dbQueryWarp(args[2], event.getGuild().getId())) {
            if (! rs.next()) {
                PlayerWarpHelper.throwWarpDoesNotExists(event);
                return;
            }
            try(PreparedStatement statement = Bot.connection.prepareStatement("""
                        SELECT *
                        FROM WARP_LISTINGS
                        WHERE warp_id = ?
                    """)){
                statement.setInt(1, rs.getInt("id"));
                ResultSet listings = statement.executeQuery();

                ArrayList<String> items = new ArrayList<>();
                while(listings.next())
                    items.add(listings.getString("name"));

                Collections.sort(items);

                if (items.isEmpty()){
                    EmbedBuilder embed = new EmbedBuilder()
                            .setAuthor("I couldn't find any listings for this player warp", null, event.getJDA().getSelfUser().getAvatarUrl());
                    event.getMessage().getChannel().sendMessageEmbeds(embed.build()).queue();
                    return;
                }

                warplistEmbed.setText("Items available at **pw " + rs.getString("name") + "**: ").setItems(items.toArray(new String[0]));
                warplistEmbed.build().display(event.getMessage().getChannel());
            }
            catch (SQLException e){
                e.printStackTrace();
            }

        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return "pw items";
    }

    @Override
    public String getDescription() {
        return "View the listings for a specific player warp \n`pw items <pw name>`";
    }
}
