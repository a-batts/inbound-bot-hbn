package main;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class HBNCommands extends ListenerAdapter {

    public void onMessageReceived(MessageReceivedEvent event){
        Message message = event.getMessage();

        if (message.getContentRaw().equals(Main.COMMAND_PREFIX + "pw"))
            message.reply("our amazing shop district is located at /pw hotboy <3").queue();

        if (message.getContentRaw().startsWith(Main.COMMAND_PREFIX + "lick"))
            message.reply("\uD83D\uDC45" + message.getMember().getEffectiveName() + " licked " + message.getContentRaw().substring(Main.COMMAND_PREFIX.length() + 5) + "\uD83D\uDC45").queue();
    }

}
