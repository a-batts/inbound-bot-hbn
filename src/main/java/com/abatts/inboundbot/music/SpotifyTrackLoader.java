package com.abatts.inboundbot.music;

import com.abatts.inboundbot.Bot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;
import se.michaelthelin.spotify.model_objects.specification.*;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import se.michaelthelin.spotify.requests.data.albums.GetAlbumRequest;
import se.michaelthelin.spotify.requests.data.playlists.GetPlaylistsItemsRequest;
import se.michaelthelin.spotify.requests.data.tracks.GetTrackRequest;

import java.awt.*;
import java.io.IOException;

public class SpotifyTrackLoader {
    private static final SpotifyApi api;

    static {
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

    public static void loadFromTrack(GuildMessageReceivedEvent event, String id){
        GetTrackRequest request = api.getTrack(id).build();
        try {
            Track track = request.execute();
            PlayerManager.getInstance()
                    .loadAndPlay(event.getChannel(), "ytsearch:" + track.getName() + " " + track.getArtists()[0].getName());
        } catch (IOException | ParseException | SpotifyWebApiException e) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setAuthor("Unable to connect to Spotify", null, event.getJDA().getSelfUser().getAvatarUrl())
                    .setColor(Color.red);
            event.getChannel().sendMessageEmbeds(embed.build()).queue();
        }
    }

    public static void loadFromAlbum(GuildMessageReceivedEvent event, String id){
        GetAlbumRequest request = api.getAlbum(id).build();
        try {
            Album album = request.execute();
            for (TrackSimplified track: album.getTracks().getItems()){
                PlayerManager.getInstance()
                        .loadAndPlay(event.getChannel(), "ytsearch:" + track.getName() + " " + track.getArtists()[0].getName(), true);
            }
        } catch (IOException | ParseException | SpotifyWebApiException e) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setAuthor("Unable to connect to Spotify", null, event.getJDA().getSelfUser().getAvatarUrl())
                    .setColor(Color.red);
            event.getChannel().sendMessageEmbeds(embed.build()).queue();
        }
    }

    public static void loadFromPlaylist(GuildMessageReceivedEvent event, String id){
        GetPlaylistsItemsRequest request = api.getPlaylistsItems(id).build();
        try {
            Paging<PlaylistTrack> paging = request.execute();
            for (PlaylistTrack track: paging.getItems()){
                PlayerManager.getInstance()
                        .loadAndPlay(event.getChannel(), "ytsearch:" + track.getTrack().getName() + " " + ((Track) track.getTrack()).getArtists()[0].getName(), true);
            }
        } catch (IOException | ParseException | SpotifyWebApiException e) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setAuthor("Unable to connect to Spotify", null, event.getJDA().getSelfUser().getAvatarUrl())
                    .setColor(Color.red);
            event.getChannel().sendMessageEmbeds(embed.build()).queue();
        }
    }
}
