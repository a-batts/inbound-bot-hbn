package main;

import net.dv8tion.jda.api.entities.Emote;
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

        if (message.getContentRaw().equals(message.getContentRaw().toUpperCase()) && message.getContentRaw().replaceAll("\\s", "").matches("([a-zA-Z]){6,}"))
            message.reply("no caps in " + message.getChannel().getName() + " please <3").queue();

        if (message.getContentRaw().toLowerCase().contains("pandabot"))
            message.reply("im sorry but the old pandabot cant come to the phone right now").queue();

        if (message.getContentRaw().toLowerCase().contains("bestie") && message.getContentRaw().toLowerCase().contains("annie"))
            message.addReaction("\u2764\ufe0f").queue();

        if (message.getContentRaw().toLowerCase().contains("coming out") || message.getContentRaw().toLowerCase().contains("come out")){
            message.addReaction("\ud83c\udff3\ufe0f\u200d\ud83c\udf08").queue();
        }
    }
}
