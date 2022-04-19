package com.abatts.inboundbot;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.REST;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;

public class Bot {
    private static final EventWaiter eventWaiter = new EventWaiter();

    public static String COMMAND_PREFIX = "$";
    public static String MUTE_ROLE = "muted";

    public static JDA jda;

    public static Flickr flickr;

    public static void main(String[] args) {
        String botToken;
        try {
            Dotenv env = Dotenv.load();
            botToken = env.get("DISCORD_BOT_TOKEN");
            flickr = new Flickr(env.get("FLICKR_API_KEY"), env.get("FLICKR_API_SECRET"), new REST());
        } catch (DotenvException e){
            botToken = System.getenv("DISCORD_BOT_TOKEN");
            flickr = new Flickr(System.getenv("FLICKR_API_KEY"), System.getenv("FLICKR_API_SECRET"), new REST());
        }

        try {
            new Bot().start(botToken);
        }
        catch (LoginException | InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    public void start(String token) throws LoginException, InterruptedException {
        JDABuilder builder = JDABuilder.createDefault(token).addEventListeners(
                new CommandListener(),
                eventWaiter
        ).setStatus(OnlineStatus.ONLINE).setActivity(Activity.watching("your capitalization"));

        jda = builder.build();
    }

    public static EventWaiter getEventWaiter() {
        return eventWaiter;
    }
}
