package com.abatts.inboundbot.command.commands.music;

import com.abatts.inboundbot.Bot;
import com.abatts.inboundbot.command.Command;
import com.abatts.inboundbot.music.GuildTrackManager;
import com.abatts.inboundbot.music.PlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchTracksRequest;

import java.awt.*;
import java.io.IOException;

public class SpotifyCommand implements Command {
    private final SpotifyApi api;

    public SpotifyCommand() {
        api = new SpotifyApi.Builder()
                .setClientId(Bot.getEnvProperty("SPOTIFY_CLIENT"))
                .setClientSecret(Bot.getEnvProperty("SPOTIFY_SECRET"))
                .build();
        final ClientCredentialsRequest request = api.clientCredentials()
                .build();

        try {
            ClientCredentials credentials = request.execute();
            api.setAccessToken(credentials.getAccessToken());

        } catch (IOException | SpotifyWebApiException | ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void runCommand(GuildMessageReceivedEvent event) {
        GuildTrackManager trackManager = PlayerManager.getInstance().getTrackManager(event.getGuild());
        AudioTrack nowPlaying = trackManager.player.getPlayingTrack();

        String trackName = nowPlaying.getInfo().title + "artist:" + nowPlaying.getInfo().author;
        SearchTracksRequest request = api.searchTracks(trackName).build();
        System.out.println(trackName);

        try {
            Track track = request.execute().getItems()[0];
            event.getChannel().sendMessage(track.getUri()).queue();
        } catch (IOException | SpotifyWebApiException | ParseException | IndexOutOfBoundsException e) {
            event.getChannel().sendMessageEmbeds(new EmbedBuilder()
                    .setAuthor("I wasn't able to find that song on Spotify", null, event.getChannel().getJDA().getSelfUser().getAvatarUrl())
                    .setColor(Color.RED).build()).queue();
        }
    }

    @Override
    public String getName() {
        return "spotify";
    }

    @Override
    public String getDescription() {
        return "Returns a Spotify link to the currently playing song";
    }
}
