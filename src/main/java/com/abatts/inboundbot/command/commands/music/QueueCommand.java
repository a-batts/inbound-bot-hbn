package com.abatts.inboundbot.command.commands.music;

import com.abatts.inboundbot.Bot;
import com.abatts.inboundbot.command.Command;
import com.abatts.inboundbot.music.GuildTrackManager;
import com.abatts.inboundbot.music.PlayerManager;
import com.jagrosh.jdautilities.menu.Paginator;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.PermissionException;

import java.awt.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("ConstantConditions")
public class QueueCommand implements Command {
    private final int SONGS_PER_PAGE = 10;
    private final Paginator.Builder queueEmbed;

    public QueueCommand(){
        this.queueEmbed = new Paginator.Builder()
                .setColumns(1)
                .setFinalAction(m -> {try{m.clearReactions().queue();}catch(PermissionException ignore){}})
                .setItemsPerPage(10)
                .waitOnSinglePage(false)
                .useNumberedItems(true)
                .showPageNumbers(true)
                .wrapPageEnds(true)
                .setEventWaiter(Bot.getEventWaiter())
                .setTimeout(1, TimeUnit.MINUTES);
    }

    @Override
    public void runCommand(GuildMessageReceivedEvent event) {
        int pagenum = 1;

        Member bot = event.getGuild().getSelfMember();
        GuildVoiceState botState = bot.getVoiceState();

        if (!botState.inVoiceChannel()){
            event.getChannel().sendMessageEmbeds(new EmbedBuilder()
                    .setAuthor("Nothing is playing right now", null, event.getChannel().getJDA().getSelfUser().getAvatarUrl())
                    .setColor(Color.RED).build()).queue();
            return;
        }

        GuildTrackManager trackManager = PlayerManager.getInstance().getTrackManager(event.getGuild());
        BlockingQueue<AudioTrack> queue = trackManager.trackScheduler.songQueue;

        if (queue.isEmpty()){
            event.getChannel().sendMessageEmbeds(new EmbedBuilder()
                    .setAuthor("Nothing is currently queued", null, event.getChannel().getJDA().getSelfUser().getAvatarUrl())
                    .setColor(Color.RED).build()).queue();
            return;
        }

        String [] songs = new String[queue.size()];
        int i = 0;
        for (AudioTrack tr: queue){
            String trackName = tr.getInfo().title;
            if (! tr.getInfo().title.contains(tr.getInfo().author))
                trackName = tr.getInfo().author + " - " + trackName;

            songs[i] = trackName;
            i++;
        }

        queueEmbed.setText(queue.size() + " song" + (queue.size() > 1 ? "s are" : " is") + " currently in the queue").setItems(songs);
        queueEmbed.build().display(event.getMessage().getChannel());
    }

    @Override
    public String getName() {
        return "queue";
    }

    @Override
    public String getDescription() {
        return "View the list of currently queued songs";
    }
}
