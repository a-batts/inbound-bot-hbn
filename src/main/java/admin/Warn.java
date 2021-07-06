package admin;

import main.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.util.List;

public class Warn extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        if (event.getMessage().getContentRaw().startsWith(Main.COMMAND_PREFIX + "warn")){
            if (event.getMessage().getMember().hasPermission(Permission.MANAGE_SERVER)){

                List<Member> members = event.getMessage().getMentionedMembers();

                String[] split = event.getMessage().getContentRaw().split("\\s+");
                String message = "";
                for (int i = members.size(); i < split.length; i ++ )
                    message += split[i] + "";

                EmbedBuilder warning = new EmbedBuilder();
                warning.setAuthor("Warning");
                warning.setTitle("You have received a warning from " + event.getMessage().getGuild().getName());
                warning.setDescription("Message: " + message);
                warning.setColor(Color.red);

                for (Member member : members){
                    member.getUser().openPrivateChannel().flatMap(channel -> channel.sendMessage(warning.build())).queue();
                }



                if (members.size() > 0){
                    EmbedBuilder embed = new EmbedBuilder();
                    embed.setAuthor("warned the mentioned users", null, event.getJDA().getSelfUser().getAvatarUrl());
                    embed.setColor(Color.red);
                    event.getMessage().getChannel().sendMessage(embed.build()).queue();
                }

            }
            else{
                EmbedBuilder embed = new EmbedBuilder();
                embed.setAuthor("you don't have the perms for that command <3", null, event.getJDA().getSelfUser().getAvatarUrl());
                embed.setColor(Color.red);
                event.getMessage().getChannel().sendMessage(embed.build()).queue();
            }
        }

    }
}