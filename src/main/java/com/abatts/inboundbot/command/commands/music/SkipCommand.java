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

@SuppressWarnings("ConstantConditions")
public class SkipCommand implements Command {
    @Override
    public void runCommand(GuildMessageReceivedEvent event) {
        Member bot = event.getGuild().getSelfMember();
        GuildVoiceState botState = bot.getVoiceState();
        GuildVoiceState userState = event.getMember().getVoiceState();

        if (!botState.inVoiceChannel()){
            event.getChannel().sendMessageEmbeds(new EmbedBuilder()
                    .setAuthor("I'm not currently playing anything", null, event.getChannel().getJDA().getSelfUser().getAvatarUrl())
                    .setColor(Color.RED).build()).queue();
            return;
        }
        if (! botState.getChannel().equals(userState.getChannel())){
            event.getChannel().sendMessageEmbeds(new EmbedBuilder()
                    .setAuthor("You need to be in the same voice channel as the bot to run this command", null, event.getChannel().getJDA().getSelfUser().getAvatarUrl())
                    .setColor(Color.RED).build()).queue();
            return;
        }

        GuildTrackManager trackManager = PlayerManager.getInstance().getTrackManager(event.getGuild());
        if (trackManager.trackScheduler.songQueue.size() == 0){
            event.getChannel().sendMessageEmbeds(new EmbedBuilder()
                    .setAuthor("No more songs to skip to", null, event.getChannel().getJDA().getSelfUser().getAvatarUrl())
                    .setColor(Color.RED).build()).queue();
            return;
        }

        trackManager.trackScheduler.nextSong();
        AudioTrack track = trackManager.player.getPlayingTrack();

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
        return "skip";
    }

    @Override
    public String getDescription() {
        return "Skip to the next song in the queue";
    }

    @Override
    public String[] getAliases(){
        return new String[]{"nextsong"};
    }
}
