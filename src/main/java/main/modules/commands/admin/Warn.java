package main.modules.commands.admin;

import main.Main;
import main.modules.permission.PermissionManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.util.List;

public class Warn extends ListenerAdapter{
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Message message = event.getMessage();
        if (message.getContentRaw().startsWith(Main.COMMAND_PREFIX + "warn")) {
            if (PermissionManager.isAdmin(message.getMember())){List<Member> members = message.getMentionedMembers();
                String[] split = message.getContentRaw().split("\\s+");
                String warnMessage = "";
                for (int i = members.size() + 1; i < split.length; i ++ )
                    warnMessage += split[i] + " ";

                EmbedBuilder warning = new EmbedBuilder();
                warning.setAuthor(message.getJDA().getSelfUser().getName());
                warning.setTitle("You have received a warning in " + message.getGuild().getName());
                if (split.length > members.size() + 1)
                    warning.setDescription("from " + message.getAuthor().getAsMention() + " : " + warnMessage);
                warning.setColor(Color.yellow);
                for (Member member : members)
                    member.getUser().openPrivateChannel().flatMap(channel -> channel.sendMessage(warning.build())).queue();

                if (members.size() > 0){
                    EmbedBuilder embed = new EmbedBuilder();
                    embed.setAuthor("warned the mentioned users <3", null, event.getJDA().getSelfUser().getAvatarUrl());
                    embed.setColor(Color.yellow);
                    message.getChannel().sendMessage(embed.build()).queue();
                }
            }
            else{
                EmbedBuilder embed = new EmbedBuilder();
                embed.setAuthor("you don't have the perms for that command <3", null, event.getJDA().getSelfUser().getAvatarUrl());
                embed.setColor(Color.red);
                message.getChannel().sendMessage(embed.build()).queue();
            }
        }
    }
}