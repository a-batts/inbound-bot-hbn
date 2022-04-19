package com.abatts.inboundbot.command.commands;

import com.abatts.inboundbot.Bot;
import com.abatts.inboundbot.command.Command;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.PhotosInterface;
import com.flickr4java.flickr.photos.SearchParameters;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;

public class RandomPandaCommand implements Command {
    @Override
    public void runCommand(GuildMessageReceivedEvent event) {
        try {
            PhotosInterface photos = Bot.flickr.getPhotosInterface();
            SearchParameters params = new SearchParameters();
            params.setText("panda zoo");
            params.setTags(new String[]{"zoo", "panda", "bear"});
            params.setSafeSearch("1");

            PhotoList<Photo> results = photos.search(params, 250, 0);
            Photo randomPhoto = results.get((int) (Math.random() * results.size()));
            event.getChannel().sendMessageEmbeds(new EmbedBuilder()
                    .setAuthor("Random panda", null, event.getChannel().getJDA().getSelfUser().getAvatarUrl())
                    .setImage(randomPhoto.getMediumUrl())
                    .setFooter("Image sourced from Flickr").build()).queue();
        } catch (FlickrException e){
            System.out.println(e.getLocalizedMessage());
            event.getChannel().sendMessageEmbeds(new EmbedBuilder()
                    .setAuthor("Error: Could not reach the Flickr servers", null, event.getChannel().getJDA().getSelfUser().getAvatarUrl())
                    .setColor(Color.RED).build()).queue();
        }
    }

    @Override
    public String getName() {
        return "panda";
    }

    @Override
    public String getDescription() {
        return "Get a random picture of a panda";
    }
}
