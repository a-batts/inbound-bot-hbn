package com.abatts.inboundbot.command;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public interface Command {

    void runCommand(GuildMessageReceivedEvent event);

    String getName();

    String getDescription();
}
