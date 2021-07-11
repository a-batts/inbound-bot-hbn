package admin;

import main.Main;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Objects;

public class EnforceCapitalization extends ListenerAdapter {

    public static boolean showingCaps = true;

    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        Message message = event.getMessage();
        if (showingCaps){
            if (message.getContentRaw().equals(message.getContentRaw().toUpperCase()) && message.getContentRaw().replaceAll("\\s", "").matches("([a-zA-Z]){7,}"))
                message.reply("no caps in " + message.getChannel().getName() + " please <3").queue();
        }
        if (message.getContentRaw().equals(Main.COMMAND_PREFIX + "ignorecaps") && Objects.requireNonNull(message.getMember()).hasPermission(Permission.MESSAGE_MANAGE)){
            message.getGuild().updateCommands().complete();
            message.getGuild().upsertCommand("unignorecaps", "Unmute my capitalization warnings (altho idk why you would want to do that)").complete();
            showingCaps = false;
            message.reply("i'll start ignoring caps <3").queue();
        }
        if (message.getContentRaw().equals(Main.COMMAND_PREFIX + "unignorecaps") && Objects.requireNonNull(message.getMember()).hasPermission(Permission.MESSAGE_MANAGE)){
            showingCaps = true;
            message.reply("no longer ignoring caps <3").queue();
        }
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event){
        if (event.getName().equals("ignorecaps")){
            if (Objects.requireNonNull(event.getMember()).hasPermission(Permission.MESSAGE_MANAGE)){
                showingCaps = false;
                event.reply("i'll start ignoring caps <3").queue();
            }
            else
                event.reply("you don't have perms for that <3").queue();

        }
        else if (event.getName().equals("unignorecaps")){
            if (Objects.requireNonNull(event.getMember()).hasPermission(Permission.MESSAGE_MANAGE)){
                showingCaps = true;
                event.reply("no longer ignoring caps <3").queue();
            }
            else
                event.reply("you don't have perms for that <3").queue();

        }
    }
}
