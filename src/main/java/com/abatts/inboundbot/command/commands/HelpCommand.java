package com.abatts.inboundbot.command.commands;

import com.abatts.inboundbot.Bot;
import com.abatts.inboundbot.command.Command;
import com.abatts.inboundbot.command.CommandsManager;
import com.jagrosh.jdautilities.menu.EmbedPaginator;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.PermissionException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class HelpCommand implements Command {
    private String name = "help";
    private int pagenum = 1;

    @Override
    public void runCommand(GuildMessageReceivedEvent event) {
        EmbedPaginator.Builder helpPaginator = new EmbedPaginator.Builder()
                .setEventWaiter(Bot.getEventWaiter())
                .setFinalAction(m -> {try{m.clearReactions().queue();}catch(PermissionException ignore){}})
                .setTimeout(1, TimeUnit.MINUTES)
                .waitOnSinglePage(false)
                .wrapPageEnds(true)
                .setText("");

        if (event.getMessage().getContentRaw().length() > Bot.COMMAND_PREFIX.length() + name.length() + 1){
            String selHelpCategory = event.getMessage().getContentRaw().substring(Bot.COMMAND_PREFIX.length() + name.length() + 1);
            switch (selHelpCategory.toLowerCase()) {
                case "music" -> pagenum = 1;
                case "fun", "misc" -> pagenum = 2;
                case "admin" -> pagenum = 3;
            }
        }

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
        helpPaginator.build().paginate(event.getMessage().getChannel(), pagenum);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return "Get help with the different commands the bot offers";
    }
}
