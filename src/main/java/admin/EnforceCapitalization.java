package admin;

import main.Main;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class EnforceCapitalization extends ListenerAdapter {

    public static boolean showingCaps = true;

    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        Message message = event.getMessage();
        if (showingCaps){
            if (message.getContentRaw().equals(message.getContentRaw().toUpperCase()) && message.getContentRaw().replaceAll("\\s", "").matches("([a-zA-Z]){7,}"))
                message.reply("no caps in " + message.getChannel().getName() + " please <3").queue();
            if (message.getContentRaw().equals(Main.COMMAND_PREFIX + "ignorecaps")){
                showingCaps = false;
                message.reply("i'll start ignoring caps <3").queue();
            }
        }
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event){
        if (event.getName().equals("ignorecaps")){
            showingCaps = false;
            event.reply("i'll start ignoring caps <3").queue();
        }
        else if (event.getName().equals("unignorecaps")){

        }
    }
}
