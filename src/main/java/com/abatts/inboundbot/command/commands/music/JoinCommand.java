package com.abatts.inboundbot.command.commands.music;

import com.abatts.inboundbot.command.Command;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class JoinCommand implements Command {
    @Override
    public void runCommand(GuildMessageReceivedEvent event) {

    }

    @Override
    public String getName() {
        return "join";
    }
}
