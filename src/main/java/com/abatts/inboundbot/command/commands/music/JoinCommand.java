package com.abatts.inboundbot.command.commands.music;

import com.abatts.inboundbot.command.Command;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

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
            message.getChannel().sendMessage("I'm already in your current voice channel").queue();
            return;
        }
        if (! userState.inVoiceChannel()){
            message.getChannel().sendMessage("You need to join a voice channel before using this command").queue();
            return;
        }
        if (botState.inVoiceChannel()){
            message.getChannel().sendMessage("I'm already in a voice channel so I can't join this one").queue();
            return;
        }

        AudioManager audioManager = event.getGuild().getAudioManager();
        audioManager.openAudioConnection(userState.getChannel());
        message.getChannel().sendMessage("Joining the `" + message.getChannel().getName() + "` voice channel").queue();
    }

    @Override
    public String getName() {
        return "join";
    }
}
