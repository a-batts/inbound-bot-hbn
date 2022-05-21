package com.abatts.inboundbot.command.commands.admin;

import com.abatts.inboundbot.Bot;
import com.abatts.inboundbot.command.Command;
import com.abatts.inboundbot.permission.PermissionManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.List;

public class ClearCommand implements Command {
    private final String name = "clear";

    @Override
    public void runCommand(GuildMessageReceivedEvent event) {
        Message message = event.getMessage();

        if (PermissionManager.canManageMessages(message.getMember())){
            TextChannel channel = event.getChannel();
            String unparsedNum = event.getMessage().getContentRaw().substring(Bot.DEFAULT_PREFIX.length() + name.length() + 1);
            if (unparsedNum.matches("\\d+")){
                int numToDelete = Integer.parseInt(unparsedNum);
                if (numToDelete < 2){
                    EmbedBuilder embed = new EmbedBuilder()
                            .setAuthor("You need to clear at least two messages", null, event.getJDA().getSelfUser().getAvatarUrl())
                            .setColor(Color.red);
                    message.getChannel().sendMessageEmbeds(embed.build()).queue();
                    return;
                }
                if (numToDelete > 100){
                    EmbedBuilder embed = new EmbedBuilder()
                            .setAuthor("Only up to 100 messages can be cleared at a given time", null, event.getJDA().getSelfUser().getAvatarUrl())
                            .setColor(Color.red);
                    message.getChannel().sendMessageEmbeds(embed.build()).queue();
                    return;
                }
                List<Message> deleteMessages  = event.getMessage().getChannel().getHistory().retrievePast(numToDelete).complete();
                channel.deleteMessages(deleteMessages).complete();

                EmbedBuilder embed = new EmbedBuilder()
                        .setAuthor("Successfully cleared the last " + unparsedNum + " messages", null, event.getJDA().getSelfUser().getAvatarUrl())
                        .setColor(Color.green);
                channel.sendMessageEmbeds(embed.build()).queue();
            }
        }
        else
            PermissionManager.throwIncorrectPermsWarning(event, message.getChannel());
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return "Clear the last x number of chats in the current channel \n`clear <number of messages to clear>`";
    }
}
