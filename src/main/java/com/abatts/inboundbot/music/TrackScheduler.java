package com.abatts.inboundbot.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TrackScheduler extends AudioEventAdapter {
    public final AudioPlayer player;
    public BlockingQueue<AudioTrack> songQueue;

    public TrackScheduler(AudioPlayer player) {
        this.player = player;
        this.songQueue = new LinkedBlockingQueue<>();
    }

    public int queue(AudioTrack track){
       if (!player.startTrack(track, true))
           songQueue.add(track);

       return songQueue.size();
    }

    public void nextSong(){
        player.startTrack(songQueue.poll(), false);
    }

    @Override
    public void onTrackEnd(AudioPlayer player, com.sedmelluq.discord.lavaplayer.track.AudioTrack track, AudioTrackEndReason reason){
        if(reason.mayStartNext){
            nextSong();
        }
    }
}