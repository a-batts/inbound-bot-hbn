package main.modules.chat;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class HBNChatResponses extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        if (event.getAuthor().isBot())
            return;
        Message message = event.getMessage();
        String rawMessage = message.getContentRaw().toLowerCase();
        if (rawMessage.contains("hi amy"))
            message.reply("my name is annie <3").queue();
        else if (rawMessage.contains("pandabot"))
            message.reply("im sorry but the old pandabot cant come to the phone right now").queue();
        else if (rawMessage.contains("0fie") || rawMessage.contains("ofie"))
            message.reply( message.getAuthor().getAsMention() + " no drug talk in chat please").queue();
        //Emote reactions
        if (message.getContentRaw().toLowerCase().contains("bestie") && message.getContentRaw().toLowerCase().contains("annie"))
            message.addReaction("\u2764\ufe0f").queue();
        else if (message.getContentRaw().toLowerCase().contains("coming out") || message.getContentRaw().toLowerCase().contains("come out"))
            message.addReaction("\ud83c\udff3\ufe0f\u200d\ud83c\udf08").queue();
    }
}
