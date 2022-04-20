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

    public static String DEFAULT_PREFIX = "$";
    public static String DEFAULT_MUTE_ROLE = "muted";
    public static String DEFAULT_DJ_ROLE = "dj";

    public static JDA jda;

    public static Flickr flickr;

    public static void main(String[] args) {
        flickr = new Flickr(getEnvProperty("FLICKR_API_KEY"), getEnvProperty("FLICKR_API_SECRET"), new REST());
        try {
            new Bot().start(getEnvProperty("DISCORD_BOT_TOKEN"));
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

    public static String getEnvProperty(String key){
        try {
            //Local .env case
            Dotenv env = Dotenv.load();
            return env.get(key);
        } catch (DotenvException e){
            //Production Heroku case
            return System.getenv(key);
        }
    }
}
