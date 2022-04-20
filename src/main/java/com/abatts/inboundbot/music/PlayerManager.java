package com.abatts.inboundbot.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerManager {
    private static PlayerManager INSTANCE;
    private final Map<Long, GuildTrackManager> musicManagers;
    private final AudioPlayerManager audioPlayerManager;

    public PlayerManager() {
        this.musicManagers = new HashMap<>();
        this.audioPlayerManager = new DefaultAudioPlayerManager();

        AudioSourceManagers.registerRemoteSources(this.audioPlayerManager);
        AudioSourceManagers.registerLocalSource(this.audioPlayerManager);
    }

    public GuildTrackManager getTrackManager(Guild guild) {
        return this.musicManagers.computeIfAbsent(guild.getIdLong(), (guildId) -> {
            GuildTrackManager trackManager = new GuildTrackManager(this.audioPlayerManager);

            guild.getAudioManager().setSendingHandler(trackManager.getHandler());

            return trackManager;
        });
    }

    public void loadAndPlay(TextChannel channel, String trackUrl){
        loadAndPlay(channel, trackUrl, false);
    }

    public void loadAndPlay(TextChannel channel, String trackUrl, boolean loadSilently) {
        GuildTrackManager trackManager = this.getTrackManager(channel.getGuild());

        this.audioPlayerManager.loadItemOrdered(trackManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                int pos = trackManager.trackScheduler.queue(track);

                if(! loadSilently){
                    long songLength = track.getInfo().length;
                    EmbedBuilder songEmbed = new EmbedBuilder().setAuthor("Song added to queue", null, channel.getJDA().getSelfUser().getAvatarUrl())
                            .setTitle(track.getInfo().title, track.getInfo().uri)
                            .setColor(Color.GREEN)
                            .addField("Artist", track.getInfo().author, true)
                            .addField("Length", (songLength/1000)/60 + ":" + String.format("%02d", (songLength/1000)%60) , true)
                            .setFooter("Currently position " + pos + " in the queue");

                    String [] split = track.getInfo().uri.split("v=");
                    songEmbed.setThumbnail("https://img.youtube.com/vi/" + split[1] + "/hqdefault.jpg");

                    channel.sendMessageEmbeds(songEmbed.build()).queue();
                }
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                List<AudioTrack> tracks = playlist.getTracks();
                AudioTrack selectedTrack = tracks.get(0);
                trackLoaded(selectedTrack);
            }

            @Override
            public void noMatches() {
                EmbedBuilder songEmbed = new EmbedBuilder().
                        setAuthor("I wasn't able to find any matches for your search", null, channel.getJDA().getSelfUser().getAvatarUrl())
                        .setColor(Color.RED);
                channel.sendMessageEmbeds(songEmbed.build()).queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                //
            }
        });
    }

    public static PlayerManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PlayerManager();
        }

        return INSTANCE;
    }
}
