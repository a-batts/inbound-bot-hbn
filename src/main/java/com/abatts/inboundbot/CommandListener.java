package com.abatts.inboundbot;

import com.abatts.inboundbot.command.CommandsManager;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CommandListener extends ListenerAdapter {

    private final CommandsManager listener = new CommandsManager();

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event){
        String message = event.getMessage().toString();
        if (message.startsWith(Bot.COMMAND_PREFIX))
            listener.runCommand(event);

    }
}
