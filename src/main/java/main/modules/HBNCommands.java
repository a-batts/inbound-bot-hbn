package main.modules;

import main.Main;
import net.dv8tion.jda.api.EmbedBuilder;
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

        if (message.getContentRaw().equals(Main.COMMAND_PREFIX + "hotdonald")){
            EmbedBuilder image = new EmbedBuilder();
            image.setImage("https://i.kym-cdn.com/entries/icons/original/000/023/327/trumptennisss.jpg");
            message.reply(image.build()).queue();
        }

        if (message.getContentRaw().startsWith(Main.COMMAND_PREFIX + "stock")){

        }

        if (message.getContentRaw().startsWith(Main.COMMAND_PREFIX + "stab")){
            message.getChannel().sendMessage("\uD83D\uDE10\n" +
                    "<|-\uD83D\uDD2A\n" +
                    "/|\n" +
                    message.getMember().getEffectiveName() + " stabbed " + message.getContentRaw().substring(Main.COMMAND_PREFIX.length() + 5)).queue();
        }
    }

}