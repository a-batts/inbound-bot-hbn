package main;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class BasicResponses extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        if (event.getAuthor().isBot())
            return;
        Message message = event.getMessage();

        if (message.getContentRaw().equalsIgnoreCase("hi amy"))
            message.reply("my name is annie <3").queue();

        if (message.getContentRaw().equals(message.getContentRaw().toUpperCase()) && message.getContentRaw().replaceAll("\\s", "").matches("([a-zA-Z]){3,}"))
            message.reply("no caps in " + message.getChannel().getName() + " please <3").queue();

        if (message.getContentRaw().toLowerCase().contains("pandabot"))
            message.reply("im sorry but the old pandabot cant come to the phone right now").queue();
    }
}
