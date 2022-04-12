package com.abatts.inboundbot.command.commands.music;

import com.abatts.inboundbot.command.Command;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class LeaveCommand implements Command {
    @SuppressWarnings("ConstantConditions")
    @Override
    public void runCommand(GuildMessageReceivedEvent event) {
        Member bot = event.getGuild().getSelfMember();
        Message message = event.getMessage();
        GuildVoiceState botState = bot.getVoiceState();

        Member user = event.getMember();
        GuildVoiceState userState = user.getVoiceState();

        if (userState.inVoiceChannel()){
            if (userState.getChannel() == botState.getChannel() && userState.getChannel() != null){
                event.getGuild().getAudioManager().closeAudioConnection();
                message.reply("Goodbye!").queue();
            }
        }
        else {
            message.getChannel().sendMessage("You need to be in the same voice channel as me to use this command").queue();
        }
    }

    @Override
    public String getName() {
        return "leave";
    }
}
