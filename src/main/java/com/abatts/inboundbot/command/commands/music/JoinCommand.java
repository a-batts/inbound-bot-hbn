package com.abatts.inboundbot.command.commands.music;

import com.abatts.inboundbot.command.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

import java.awt.*;

public class JoinCommand implements Command {
    @SuppressWarnings("ConstantConditions")
    @Override
    public void runCommand(GuildMessageReceivedEvent event) {
        Member bot = event.getGuild().getSelfMember();
        Message message = event.getMessage();
        GuildVoiceState botState = bot.getVoiceState();

        Member user = event.getMember();
        GuildVoiceState userState = user.getVoiceState();

        if (userState.getChannel() == botState.getChannel() && userState.getChannel() != null){
            event.getChannel().sendMessageEmbeds(new EmbedBuilder()
                    .setAuthor("I'm already in your current voice channel", null, event.getChannel().getJDA().getSelfUser().getAvatarUrl())
                    .setColor(Color.RED).build()).queue();
            return;
        }
        if (! userState.inVoiceChannel()){
            event.getChannel().sendMessageEmbeds(new EmbedBuilder()
                    .setAuthor("You need to join a voice channel before using this command", null, event.getChannel().getJDA().getSelfUser().getAvatarUrl())
                    .setColor(Color.RED).build()).queue();
            return;
        }
        if (botState.inVoiceChannel()){
            event.getChannel().sendMessageEmbeds(new EmbedBuilder()
                    .setAuthor("I'm already in a voice channel so I can't join this one", null, event.getChannel().getJDA().getSelfUser().getAvatarUrl())
                    .setColor(Color.RED).build()).queue();
            return;
        }

        AudioManager audioManager = event.getGuild().getAudioManager();
        audioManager.openAudioConnection(userState.getChannel());
        event.getChannel().sendMessageEmbeds(new EmbedBuilder()
                .setAuthor("Connected to the " + message.getChannel().getName() + " voice channel", null, event.getChannel().getJDA().getSelfUser().getAvatarUrl())
                .setColor(Color.GREEN).build()).queue();
    }

    @Override
    public String getName() {
        return "join";
    }

    @Override
    public String getDescription() {
        return "Have the bot join your current voice channel \n`join`";
    }
}
