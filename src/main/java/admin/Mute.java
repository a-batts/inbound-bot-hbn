package admin;

import main.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.util.List;

public class Mute extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getMessage().getContentRaw().startsWith(Main.COMMAND_PREFIX + "muteuser")){
            if (event.getMessage().getMember().hasPermission(Permission.MANAGE_ROLES)){
                List<Member> members = event.getMessage().getMentionedMembers();

                Role muteRole = event.getGuild().getRolesByName("muted", false).get(0);
                for(Member member: members){
                    event.getGuild().addRoleToMember(member, muteRole);
                }
                EmbedBuilder embed = new EmbedBuilder();
                embed.setAuthor("muted the mentioned users <3", null, event.getJDA().getSelfUser().getAvatarUrl());
                embed.setColor(Color.green);
                event.getMessage().getChannel().sendMessage(embed.build()).queue();

            }
            else{
                EmbedBuilder embed = new EmbedBuilder();
                embed.setAuthor("you don't have the perms for that command <3", null, event.getJDA().getSelfUser().getAvatarUrl());
                embed.setColor(Color.red);
                event.getMessage().getChannel().sendMessage(embed.build()).queue();
            }
        }
        if (event.getMessage().getContentRaw().startsWith(Main.COMMAND_PREFIX + "unmuteuser")){

        }
    }
}
