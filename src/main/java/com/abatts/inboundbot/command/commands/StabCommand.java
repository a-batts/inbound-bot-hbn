package com.abatts.inboundbot.command.commands;

import com.abatts.inboundbot.Bot;
import com.abatts.inboundbot.command.Command;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class StabCommand implements Command {
    private final String name = "stab";

    @Override
    public void runCommand(GuildMessageReceivedEvent event) {
        Message message = event.getMessage();
        message.getChannel().sendMessage("\uD83D\uDE10\n" +
                "<|-\uD83D\uDD2A\n" +
                "/|\n" +
                message.getMember().getEffectiveName() + " stabbed " + message.getContentRaw().substring(Bot.COMMAND_PREFIX.length() + name.length() + 1)).queue();
    }

    @Override
    public String getName() {
        return name;
    }
}
