package com.abatts.inboundbot.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

public class GuildTrackManager {
    public final AudioPlayer player;
    public final TrackScheduler trackScheduler;
    private final AudioPlayerSendHandler handler;

    public GuildTrackManager(AudioPlayerManager playerManager) {
        player = playerManager.createPlayer();
        trackScheduler = new TrackScheduler(player);
        handler = new AudioPlayerSendHandler(player);
        player.addListener(trackScheduler);
    }

    public AudioPlayerSendHandler getHandler() {
        return handler;
    }
}
