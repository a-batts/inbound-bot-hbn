package com.abatts.inboundbot.command.commands.music;

import com.abatts.inboundbot.command.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;

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
                event.getChannel().sendMessageEmbeds(new EmbedBuilder()
                        .setAuthor("Goodbye!", null, event.getChannel().getJDA().getSelfUser().getAvatarUrl())
                        .setColor(Color.GREEN).build()).queue();
            }
        }
        else {
            event.getChannel().sendMessageEmbeds(new EmbedBuilder()
                    .setAuthor("You need to be in the same voice channel as the bot to run this command", null, event.getChannel().getJDA().getSelfUser().getAvatarUrl())
                    .setColor(Color.RED).build()).queue();
        }
    }

    @Override
    public String getName() {
        return "leave";
    }

    @Override
    public String getDescription() {
        return "Have the bot leave your current voice channel";
    }
}
