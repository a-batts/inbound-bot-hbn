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
        if (event.getMessage().getContentRaw().equals(Main.COMMAND_PREFIX + "warn")){
            EmbedBuilder embed = new EmbedBuilder();
            embed.setAuthor("you have received a warning from " + event.getMessage().getGuild().getName());
            embed.setColor(Color.red);

            List<Member> members = event.getMessage().getMentionedMembers();
            for (Member member : members){
                member.getUser().openPrivateChannel().flatMap(channel -> channel.sendMessage(embed.build())).queue();
            }

        }
    }
}