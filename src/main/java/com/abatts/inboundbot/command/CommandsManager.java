package com.abatts.inboundbot.command;

import com.abatts.inboundbot.Bot;
import com.abatts.inboundbot.command.commands.*;
import com.abatts.inboundbot.command.commands.admin.*;
import com.abatts.inboundbot.command.commands.music.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.List;

public class CommandsManager extends ListenerAdapter {
    private final List<Command> commands = new ArrayList<>();

    public CommandsManager(){
        addCommands();
    }

    private void addCommands(){
        //HBN commands
        commands.add(new PwCommand());
        commands.add(new LickCommand());
        commands.add(new StabCommand());

        //Admin commands
        commands.add(new WarnCommand());
        commands.add(new ClearCommand());
        commands.add(new MuteCommand());
        commands.add(new UnmuteCommand());

        //Music commands
        commands.add(new JoinCommand());
        commands.add(new LeaveCommand());
        commands.add(new PlayCommand());
        commands.add(new StopCommand());
        commands.add(new NowPlayingCommand());
        commands.add(new SkipCommand());
        commands.add(new RestartCommand());
        commands.add(new QueueCommand());
    }

    public Command getCommand(String searchTerm){
        for(Command c: commands){
            if (c.getName().equals(searchTerm))
                return c;
        }
        return null;
    }

    public void runCommand(GuildMessageReceivedEvent event){
        String message = event.getMessage().getContentRaw().substring(Bot.COMMAND_PREFIX.length());
        Command command = getCommand(message.split("\\s+")[0].toLowerCase());

        if (command != null){
            event.getChannel().sendTyping().queue();
            command.runCommand(event);
        }
    }

}
