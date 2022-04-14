package com.abatts.inboundbot.command.commands;

import com.abatts.inboundbot.Bot;
import com.abatts.inboundbot.command.Command;
import com.abatts.inboundbot.command.CommandsManager;
import com.abatts.inboundbot.command.commands.music.*;
import com.jagrosh.jdautilities.menu.EmbedPaginator;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.PermissionException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class HelpCommand implements Command {
    @Override
    public void runCommand(GuildMessageReceivedEvent event) {
        EmbedPaginator.Builder helpPaginator = new EmbedPaginator.Builder()
                .setEventWaiter(Bot.getEventWaiter())
                .setFinalAction(m -> {try{m.clearReactions().queue();}catch(PermissionException ignore){}})
                .setTimeout(1, TimeUnit.MINUTES)
                .waitOnSinglePage(false)
                .wrapPageEnds(true)
                .setText("");

        List<MessageEmbed> embeds = new ArrayList<>();
        EmbedBuilder musicCommands = new EmbedBuilder().setAuthor("Music commands", null, event.getChannel().getJDA().getSelfUser().getAvatarUrl());
        for (Command c: CommandsManager.getCommandsByCategory(CommandCategory.MUSIC))
            musicCommands.addField("$" + c.getName(), c.getDescription(), false);
        embeds.add(musicCommands.build());

        EmbedBuilder basicCommands = new EmbedBuilder().setAuthor("Fun commands", null, event.getChannel().getJDA().getSelfUser().getAvatarUrl());
        for (Command c: CommandsManager.getCommandsByCategory(CommandCategory.HBN))
            basicCommands.addField("$" + c.getName(), c.getDescription(), false);
        embeds.add(basicCommands.build());

        EmbedBuilder adminCommands = new EmbedBuilder().setAuthor("Admin commands", null, event.getChannel().getJDA().getSelfUser().getAvatarUrl());
        for (Command c: CommandsManager.getCommandsByCategory(CommandCategory.ADMIN))
            adminCommands.addField("$" + c.getName(), c.getDescription(), false);
        embeds.add(adminCommands.build());

        helpPaginator.setItems(embeds);
        helpPaginator.build().display(event.getMessage().getChannel());
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "Get help with the different commands the bot offers";
    }
}
