package admin;

import main.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;

public class ClearChat extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        if (event.getMessage().getContentRaw().equals(Main.COMMAND_PREFIX + "clearchat")){
            if (event.getMessage().getMember().hasPermission(Permission.MANAGE_SERVER)){
                event.getMessage().getChannel().sendMessage(". \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n .").queue();
                event.getMessage().reply("so sorry about that guys <3").queue();
            }
            else{
                EmbedBuilder embed = new EmbedBuilder();
                embed.setAuthor("you don't have the perms for that command <3");
                embed.setColor(Color.red);
                event.getMessage().getChannel().sendMessage(embed.build()).queue();
            }

        }
    }
}
