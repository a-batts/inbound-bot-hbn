package com.abatts.inboundbot.command;

import com.abatts.inboundbot.Bot;
import com.abatts.inboundbot.command.commands.*;
import com.abatts.inboundbot.command.commands.admin.*;
import com.abatts.inboundbot.command.commands.hbn.*;
import com.abatts.inboundbot.command.commands.music.*;
import com.abatts.inboundbot.command.commands.playerwarps.*;
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
        //Admin commands
        commands.addAll(getCommandsByCategory(CommandCategory.ADMIN));
        //Base commands
        commands.addAll(getCommandsByCategory(CommandCategory.BASE));
        //HBN commands
        commands.addAll(getCommandsByCategory(CommandCategory.HBN));
        //Music commands
        commands.addAll(getCommandsByCategory(CommandCategory.MUSIC));
        //PW commands
        commands.addAll(getCommandsByCategory(CommandCategory.PW));

    }

    public Command getCommand(String q){
        for(Command c: commands){
            if ((c.getName().length() == q.length() && q.equals(c.getName())) || q.startsWith(c.getName() + " "))
                return c;
            for (String a: c.getAliases()){
                if ((a.length() == q.length() && q.equals(a)) || q.startsWith(a + " "))
                    return c;
            }
        }
        return null;
    }

    public void runCommand(GuildMessageReceivedEvent event){
        String message = event.getMessage().getContentRaw().substring(Bot.DEFAULT_PREFIX.length());
        Command command = getCommand(message.toLowerCase());

        if (command != null){
            event.getChannel().sendTyping().queue();
            command.runCommand(event);
        }
    }

    public static List<Command> getCommandsByCategory(CommandCategory category){
        List<Command> commands = new ArrayList<>();

        switch (category) {
            case ADMIN -> {
                commands.add(new ClearCommand());
                commands.add(new MuteCommand());
                commands.add(new UnmuteCommand());
                commands.add(new WarnCommand());
                commands.add(new ViewWarnsCommand());
            }
            case BASE -> {
                commands.add(new HelpCommand());
                commands.add(new GetAvatarCommand());
                commands.add(new RandomPandaCommand());
            }
            case HBN -> {
                commands.add(new BonkCommand());
                commands.add(new LickCommand());
                commands.add(new StabCommand());
            }
            case MUSIC -> {
                commands.add(new JoinCommand());
                commands.add(new LeaveCommand());
                commands.add(new PlayCommand());
                commands.add(new StopCommand());
                commands.add(new SkipCommand());
                commands.add(new RestartCommand());
                commands.add(new NowPlayingCommand());
                commands.add(new QueueCommand());
                commands.add(new PlayNowCommand());
                commands.add(new DequeueCommand());
                //Doesn't work currently
                //commands.add(new SpotifyCommand());
            }
            case PW -> {
                commands.add(new AddWarpCommand());
                commands.add(new DeleteWarpCommand());
                commands.add(new EditWarpCommand());
                commands.add(new ListWarpCommand());
                commands.add(new AddWarpListingCommand());
                commands.add(new RemoveWarpListingCommand());
                commands.add(new SearchWarpListingsCommand());
                commands.add(new ViewWarpListingsCommand());
            }
        }

        return commands;
    }

}
