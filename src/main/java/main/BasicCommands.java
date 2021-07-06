package main;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class BasicCommands extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        if (event.getAuthor().isBot())
            return;
        Message message = event.getMessage();

        if (message.getContentRaw().equalsIgnoreCase("hi amy"))
            message.reply("my name is annie <3").queue();

        if (message.getContentRaw().equals(message.getContentRaw().toUpperCase()) && message.getContentRaw().matches("([a-zA-Z]){3,}"))
            message.reply("no caps in " + message.getChannel().getName() + " please <3").queue();
    }
}
