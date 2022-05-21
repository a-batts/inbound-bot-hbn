package com.abatts.inboundbot.command.commands.music;

import com.abatts.inboundbot.command.Command;
import com.abatts.inboundbot.music.GuildTrackManager;
import com.abatts.inboundbot.music.PlayerManager;
import com.abatts.inboundbot.permission.PermissionManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.LinkedList;

public class PlayNowCommand implements Command {
    @Override
    public void runCommand(GuildMessageReceivedEvent event) {
        Member member = event.getMessage().getMember();
        if (PermissionManager.Music.canManageMusic(member)) {
            GuildTrackManager trackManager = PlayerManager.getInstance().getTrackManager(event.getGuild());
            trackManager.trackScheduler.player.stopTrack();

            LinkedList<AudioTrack> tracks = new LinkedList<>(trackManager.trackScheduler.songQueue);
            trackManager.trackScheduler.songQueue.clear();
            new PlayCommand().runCommand(event);
            trackManager.trackScheduler.songQueue.addAll(tracks);
            trackManager.player.setPaused(false);

        }
        else
            PermissionManager.Music.throwIncorrectPermsWarning(event, event.getChannel());
    }

    @Override
    public String getName() {
        return "playnow";
    }

    @Override
    public String getDescription() {
        return "Play a song immediately instead of adding it to the queue \n`playnow <song name or youtube or spotify url>`";
    }
}
