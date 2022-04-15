package com.abatts.inboundbot;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.REST;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
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

    private static final String FLICKR_API_KEY = "***REMOVED***";
    private static final String FLICKR_API_SECRET = "***REMOVED***";

    public static final Flickr FLICKR = new Flickr(FLICKR_API_KEY, FLICKR_API_SECRET, new REST());

    public static void main(String[] args) {
        try
        {
            new Bot().start("***REMOVED***");
        }
        catch (LoginException | InterruptedException ex)
        {
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
