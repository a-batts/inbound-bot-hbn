package main.modules.commands.admin;

import main.Main;
import main.modules.permission.PermissionManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.util.List;

public class Trim extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        Message message = event.getMessage();
        if (message.getContentRaw().startsWith(Main.COMMAND_PREFIX + "trim")){
            if (PermissionManager.canManageMessages(message.getMember())){
                TextChannel channel = event.getTextChannel();
                String amount = event.getMessage().getContentRaw().substring(Main.COMMAND_PREFIX.length() + 5);
                if (amount.matches("\\d+")){
                    int numToDelete = Integer.parseInt(amount);
                    if (numToDelete < 2){
                        event.getMessage().getChannel().sendMessage("you need to trim at least two messages").queue();
                        return;
                    }
                    List<Message> deleteMessages  = event.getMessage().getChannel().getHistory().retrievePast(numToDelete).complete();
                    channel.deleteMessages(deleteMessages).complete();

                    EmbedBuilder embed = new EmbedBuilder();
                    embed.setAuthor("trimmed the last " + amount + " messages <3", null, event.getJDA().getSelfUser().getAvatarUrl());
                    embed.setColor(Color.green);
                    channel.sendMessage(embed.build()).queue();
                }
            }
            else{
                EmbedBuilder embed = new EmbedBuilder();
                embed.setAuthor("sorry i was unable to trim the requested messages :(", null, event.getJDA().getSelfUser().getAvatarUrl());
                embed.setColor(Color.red);
                event.getMessage().getChannel().sendMessage(embed.build()).queue();
            }
        }
    }
}
