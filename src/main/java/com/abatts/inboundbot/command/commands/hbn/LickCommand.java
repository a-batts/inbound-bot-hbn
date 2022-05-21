package com.abatts.inboundbot.command.commands.hbn;

import com.abatts.inboundbot.Bot;
import com.abatts.inboundbot.command.Command;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class LickCommand implements Command {
    private final String name = "lick";

    @Override
    public void runCommand(GuildMessageReceivedEvent event) {
        Message message = event.getMessage();
        message.getChannel().sendMessage("\uD83D\uDC45" + message.getMember().getEffectiveName() + " licked "
                + message.getContentRaw().substring(Bot.DEFAULT_PREFIX.length()
                + name.length() + 1) + "\uD83D\uDC45").queue();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return "Lick another user \n`lick <user to lick>`";
    }
}
