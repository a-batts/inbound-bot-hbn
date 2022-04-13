package com.abatts.inboundbot.command.commands.music;

import com.abatts.inboundbot.Bot;
import com.abatts.inboundbot.command.Command;
import com.abatts.inboundbot.music.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.net.URI;
import java.net.URISyntaxException;

@SuppressWarnings("ConstantConditions")
public class PlayCommand implements Command {
    private final String name = "play";

    @Override
    public void runCommand(GuildMessageReceivedEvent event) {
        Member bot = event.getGuild().getSelfMember();
        Message message = event.getMessage();
        GuildVoiceState botState = bot.getVoiceState();
        GuildVoiceState userState = event.getMember().getVoiceState();

        if (message.getContentRaw().split(" ").length == 1){
            event.getChannel().sendMessageEmbeds(new EmbedBuilder()
                    .setAuthor("Please specify the song to play", null, event.getChannel().getJDA().getSelfUser().getAvatarUrl())
                    .setColor(Color.RED).build()).queue();
            return;
        }

        if (!botState.inVoiceChannel()){
            //Have bot join user's current channel if not in one
            if (userState.inVoiceChannel())
                new JoinCommand().runCommand(event);
            else{
                event.getChannel().sendMessageEmbeds(new EmbedBuilder()
                        .setAuthor("You need to join a voice channel before running this command", null, event.getChannel().getJDA().getSelfUser().getAvatarUrl())
                        .setColor(Color.RED).build()).queue();
                return;
            }
        }

        String searchTerm = message.getContentRaw().substring(Bot.COMMAND_PREFIX.length() + name.length() + 1);
        if (!isUrl(searchTerm)){
            searchTerm = "ytsearch:" + searchTerm;
        }

        PlayerManager.getInstance().loadAndPlay(event.getMessage().getTextChannel(), searchTerm);
    }

    @Override
    public String getName() {
        return name;
    }

    private boolean isUrl(String url){
        try {
            new URI(url);
            return true;
        } catch (URISyntaxException e){
            return false;
        }
    }
}
