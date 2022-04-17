package com.abatts.inboundbot.command.commands.hbn;

import com.abatts.inboundbot.command.Command;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class PwCommand implements Command {

    @Override
    public void runCommand(GuildMessageReceivedEvent event) {
        Message message = event.getMessage();
        message.reply("our amazing shop district is located at /pw hotboy <3").queue();
    }

    @Override
    public String getName() {
        return "pw";
    }

    @Override
    public String getDescription() {
        return "Get information about the HBN playerwarp";
    }
}