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
        if (event.getMessage().getMember().hasPermission(Permission.MANAGE_SERVER)){
            if (event.getMessage().getContentRaw().startsWith(Main.COMMAND_PREFIX + "warn")){
                EmbedBuilder embed = new EmbedBuilder();
                embed.setAuthor("you have received a warning from " + event.getMessage().getGuild().getName());
                embed.setColor(Color.red);

                List<Member> members = event.getMessage().getMentionedMembers();
                for (Member member : members){
                    member.getUser().openPrivateChannel().flatMap(channel -> channel.sendMessage(embed.build())).queue();
                }

                if (members.size() > 0){
                    embed.setAuthor("warned the mentioned users");
                    embed.setColor(Color.red);
                    event.getMessage().getChannel().sendMessage(embed.build()).queue();
                }

            }
        }
        else{
            EmbedBuilder embed = new EmbedBuilder();
            embed.setAuthor("you don't have the perms for that command <3");
            embed.setColor(Color.red);
            event.getMessage().getChannel().sendMessage(embed.build()).queue();
        }
    }
}