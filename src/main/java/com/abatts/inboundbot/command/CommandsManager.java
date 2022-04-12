package com.abatts.inboundbot.command;

import com.abatts.inboundbot.Bot;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.List;

public class CommandsManager extends ListenerAdapter {
    private List<Command> commands = new ArrayList<>();

    public Command getCommand(String searchTerm){
        for(Command c: commands){
            if (c.getName().equals(searchTerm))
                return c;
        }
        return null;
    }

    public void runCommand(GuildMessageReceivedEvent event){
        String message = event.getMessage().toString().substring(Bot.COMMAND_PREFIX.length());
        Command command = getCommand(message.split(" ")[0].toLowerCase());

        if (command != null){
            event.getChannel().sendTyping().queue();

            command.runCommand(event);
        }
    }

}
