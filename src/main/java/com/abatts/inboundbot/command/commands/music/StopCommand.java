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
public class StopCommand implements Command {
    @Override
    public void runCommand(GuildMessageReceivedEvent event) {
        Member bot = event.getGuild().getSelfMember();
        GuildVoiceState botState = bot.getVoiceState();
        GuildVoiceState userState = event.getMember().getVoiceState();

        if (!botState.inVoiceChannel()){
            event.getChannel().sendMessageEmbeds(new EmbedBuilder()
                    .setAuthor("Nothing is playing right now", null, event.getChannel().getJDA().getSelfUser().getAvatarUrl())
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
        trackManager.trackScheduler.player.stopTrack();
        trackManager.trackScheduler.songQueue.clear();

        event.getChannel().sendMessageEmbeds(new EmbedBuilder()
                .setAuthor("Stopped playback", null, event.getChannel().getJDA().getSelfUser().getAvatarUrl())
                .setColor(Color.GREEN).build()).queue();
    }

    @Override
    public String getName() {
        return "stop";
    }

    @Override
    public String getDescription() {
        return "Stop the currently playing music";
    }
}
