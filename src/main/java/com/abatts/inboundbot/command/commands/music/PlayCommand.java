package com.abatts.inboundbot.command.commands.music;

import com.abatts.inboundbot.Bot;
import com.abatts.inboundbot.command.Command;
import com.abatts.inboundbot.music.PlayerManager;
import com.abatts.inboundbot.music.SpotifyTrackLoader;
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

        String [] args = message.getContentRaw().substring(0, Bot.DEFAULT_PREFIX.length()).split(" ");
        String searchTerm = message.getContentRaw().substring(Bot.DEFAULT_PREFIX.length() + args[0].length());

        if (searchTerm.contains("spotify.com")){
            fetchFromSpotify(event, searchTerm);
            return;
        }
        if (!isUrl(searchTerm))
            searchTerm = "ytsearch:" + searchTerm;
        PlayerManager.getInstance().loadAndPlay(event.getMessage().getTextChannel(), searchTerm);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return "Play a specified song or YouTube link";
    }

    @Override
    public String [] getAliases(){
        return new String[]{"p", "playsong", "enqueue"};
    }

    private boolean isUrl(String url){
        try {
            new URI(url);
            return true;
        } catch (URISyntaxException e){
            return false;
        }
    }

    private void fetchFromSpotify(GuildMessageReceivedEvent event, String url){
        url = url.replace("https://", "");
        String id = url.split("/")[2];
        if (id.contains("?"))
            id = id.substring(0, id.indexOf("?"));

        System.out.println(id);

        if (url.contains("track"))
            SpotifyTrackLoader.loadFromTrack(event, id);
        if (url.contains("album"))
            SpotifyTrackLoader.loadFromAlbum(event, id);
        if (url.contains("playlist"))
            SpotifyTrackLoader.loadFromPlaylist(event, id);
    }
}
