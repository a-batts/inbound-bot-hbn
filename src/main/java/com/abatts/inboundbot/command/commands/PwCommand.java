package com.abatts.inboundbot.command.commands;

import com.abatts.inboundbot.command.Command;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class PwCommand implements Command {
    private final String name = "pw";

    @Override
    public void runCommand(GuildMessageReceivedEvent event) {
        Message message = event.getMessage();
        message.reply("our amazing shop district is located at /pw hotboy <3").queue();
    }

    @Override
    public String getName() {
        return name;
    }
}
