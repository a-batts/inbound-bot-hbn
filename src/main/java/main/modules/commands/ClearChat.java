package main.modules.commands;

import main.Main;
import main.modules.permission.PermissionManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;

public class ClearChat extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        Message message = event.getMessage();
        if (message.getContentRaw().equals(Main.COMMAND_PREFIX + "clearchat")){
            if (PermissionManager.isAdmin(message.getMember())){
                message.getChannel().sendMessage(". \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n .").queue();
                message.reply("so sorry about that guys <3").queue();
            }
            else{
                EmbedBuilder embed = new EmbedBuilder();
                embed.setAuthor("you don't have the perms for that command <3");
                embed.setColor(Color.red);
                message.getChannel().sendMessage(embed.build()).queue();
            }
        }
    }
}
