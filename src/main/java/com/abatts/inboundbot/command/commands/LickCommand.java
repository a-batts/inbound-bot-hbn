package com.abatts.inboundbot.command.commands;

import com.abatts.inboundbot.Bot;
import com.abatts.inboundbot.command.Command;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class LickCommand implements Command {
    private final String name = "lick";

    @Override
    public void runCommand(GuildMessageReceivedEvent event) {
        Message message = event.getMessage();
        message.reply("\uD83D\uDC45" + message.getMember().getEffectiveName() + " licked "
                + message.getContentRaw().substring(Bot.COMMAND_PREFIX.length()
                + name.length() + 1) + "\uD83D\uDC45").queue();
    }

    @Override
    public String getName() {
        return name;
    }
}
