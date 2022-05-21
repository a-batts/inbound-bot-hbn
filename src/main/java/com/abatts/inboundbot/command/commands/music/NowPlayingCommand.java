package com.abatts.inboundbot.command.commands.music;

import com.abatts.inboundbot.command.Command;
import com.abatts.inboundbot.music.GuildTrackManager;
import com.abatts.inboundbot.music.PlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.Objects;

public class NowPlayingCommand implements Command {
    @SuppressWarnings("DuplicatedCode")
    @Override
    public void runCommand(GuildMessageReceivedEvent event) {
        Member bot = event.getGuild().getSelfMember();
        GuildVoiceState botState = bot.getVoiceState();

        if (!Objects.requireNonNull(botState).inVoiceChannel()){
            event.getChannel().sendMessageEmbeds(new EmbedBuilder()
                    .setAuthor("Nothing is playing right now", null, event.getChannel().getJDA().getSelfUser().getAvatarUrl())
                    .setColor(Color.RED).build()).queue();
            return;
        }

        GuildTrackManager trackManager = PlayerManager.getInstance().getTrackManager(event.getGuild());
        AudioTrack track = trackManager.player.getPlayingTrack();
        if (track == null){
            event.getChannel().sendMessageEmbeds(new EmbedBuilder()
                    .setAuthor("Nothing is playing right now", null, event.getChannel().getJDA().getSelfUser().getAvatarUrl())
                    .setColor(Color.RED).build()).queue();
            return;
        }
        long songLength = track.getInfo().length;
        EmbedBuilder songEmbed = new EmbedBuilder().setAuthor("Now playing", null, event.getChannel().getJDA().getSelfUser().getAvatarUrl())
                .setTitle(track.getInfo().title, track.getInfo().uri)
                .setColor(Color.GREEN)
                .addField("Artist", track.getInfo().author, true)
                .addField("Length", (songLength/1000)/60 + ":" + String.format("%02d", (songLength/1000)%60) , true);

        String [] split = track.getInfo().uri.split("v=");
        songEmbed.setThumbnail("https://img.youtube.com/vi/" + split[1] + "/hqdefault.jpg");

        event.getChannel().sendMessageEmbeds(songEmbed.build()).queue();
    }

    @Override
    public String getName() {
        return "nowplaying";
    }

    @Override
    public String getDescription() {
        return "Display the song that is currently playing \n`nowplaying`";
    }
}
