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
        message.getChannel().sendMessage("BOONK!" + message.getMember().getEffectiveName() + " bonked "
                + message.getContentRaw().substring(Bot.COMMAND_PREFIX.length()
                + name.length() + 1)).queue();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return "Lick the typed user";
    }
}
