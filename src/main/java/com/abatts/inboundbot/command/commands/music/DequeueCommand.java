package com.abatts.inboundbot.command.commands.music;

import com.abatts.inboundbot.command.Command;
import com.abatts.inboundbot.music.GuildTrackManager;
import com.abatts.inboundbot.music.PlayerManager;
import com.abatts.inboundbot.permission.PermissionManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

public class DequeueCommand implements Command {
    @Override
    public void runCommand(GuildMessageReceivedEvent event) {
        Member member = event.getMessage().getMember();
        if (PermissionManager.Music.canManageMusic(member)) {
            String [] args = event.getMessage().getContentRaw().split(" ");
            if (args[1] == null){
                event.getChannel().sendMessageEmbeds(new EmbedBuilder()
                        .setAuthor("You need to specify the queue position of the song to dequeue", null, event.getChannel().getJDA().getSelfUser().getAvatarUrl())
                        .setColor(Color.RED).build()).queue();
                return;
            }
            int songToRemove = Integer.parseInt(args[1]);

            GuildTrackManager trackManager = PlayerManager.getInstance().getTrackManager(event.getGuild());
            if (songToRemove < trackManager.trackScheduler.songQueue.size()){
                LinkedList<AudioTrack> tracks = new LinkedList<>(trackManager.trackScheduler.songQueue);
                AudioTrack removedTrack = tracks.remove(songToRemove);
                trackManager.trackScheduler.songQueue = new LinkedBlockingQueue<>(tracks);

                event.getChannel().sendMessageEmbeds(new EmbedBuilder()
                        .setAuthor("Successfully removed " + removedTrack.getInfo().title + " from the queue", null, event.getChannel().getJDA().getSelfUser().getAvatarUrl())
                        .setColor(Color.GREEN).build()).queue();
            }
            event.getChannel().sendMessageEmbeds(new EmbedBuilder()
                    .setAuthor("You need to pick a valid position in the queue", null, event.getChannel().getJDA().getSelfUser().getAvatarUrl())
                    .setColor(Color.RED).build()).queue();
        }
        else
            PermissionManager.Music.throwIncorrectPermsWarning(event, event.getChannel());
    }

    @Override
    public String getName() {
        return "dequeue";
    }

    @Override
    public String getDescription() {
        return "Remove a specified song from the queue";
    }
}
