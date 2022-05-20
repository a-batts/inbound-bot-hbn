package com.abatts.inboundbot.util;

import com.abatts.inboundbot.Bot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerWarpHelper {
    public static ResultSet dbQueryWarp(String name, String guild_id) throws SQLException {
        PreparedStatement statement = Bot.connection.prepareStatement("""
                SELECT * FROM PLAYER_WARPS WHERE name = ? AND guild_id = ?
        """);
        statement.setString(1, name);
        statement.setString(2, guild_id);
        return statement.executeQuery();
    }

    public static void throwWarpDoesNotExists(GuildMessageReceivedEvent event){
        EmbedBuilder embed = new EmbedBuilder()
                .setAuthor("I couldn't find a player warp in this server with that name", null, event.getJDA().getSelfUser().getAvatarUrl())
                .setColor(Color.red);
        event.getMessage().getChannel().sendMessageEmbeds(embed.build()).queue();
    }
}
