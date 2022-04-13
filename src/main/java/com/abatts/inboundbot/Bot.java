package com.abatts.inboundbot;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;

public class Bot {
    private static final EventWaiter eventWaiter = new EventWaiter();

    public static String COMMAND_PREFIX = "$";
    public static String MUTE_ROLE = "muted";

    public static JDA jda;

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
