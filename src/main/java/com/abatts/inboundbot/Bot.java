package com.abatts.inboundbot;

import com.abatts.inboundbot.command.CommandsManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;

public class Bot extends ListenerAdapter {

    public static String COMMAND_PREFIX = "!";
    public static String MUTE_ROLE = "muted";

    public static JDA jda;

    public static void main(String[] args) throws LoginException {
        String token = "***REMOVED***";
        JDABuilder builder = JDABuilder.createDefault(token);builder.setToken(token);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.watching("your capitalization"));

        builder.addEventListeners(new CommandsManager());
        /*
        builder.addEventListeners(new HBNChatResponses());
        builder.addEventListeners(new ClearChat());
        builder.addEventListeners(new HBNCommands());
        builder.addEventListeners(new Trim());
        builder.addEventListeners(new Warn());
        builder.addEventListeners(new Mute());
        builder.addEventListeners(new EnforceCapitalization());
         */

        jda = builder.build();
    }
}
