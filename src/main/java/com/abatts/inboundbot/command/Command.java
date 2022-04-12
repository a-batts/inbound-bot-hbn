package com.abatts.inboundbot.command;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public interface Command {
    String name = null;

    void runCommand(GuildMessageReceivedEvent event);

    default String getName(){
        return name;
    }
}
