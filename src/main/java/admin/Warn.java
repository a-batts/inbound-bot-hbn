package admin;

import main.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;

public class Warn extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        if (event.getMessage().getContentRaw().equals(Main.COMMAND_PREFIX + "warn")){


        }
    }
}