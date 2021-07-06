package main;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;

import main.BasicCommands;
import admin.*;

public class Main extends ListenerAdapter {

    public static String COMMAND_PREFIX = "!";

    public static void main(String[] args) throws LoginException {
        String token = "***REMOVED***";
        JDABuilder builder = JDABuilder.createDefault(token);builder.setToken(token);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.addEventListeners(new BasicCommands());
        builder.addEventListeners(new ClearChat());
        builder.addEventListeners(new HBNCommands());
        builder.addEventListeners(new Trim());

        builder.build();
    }
}
