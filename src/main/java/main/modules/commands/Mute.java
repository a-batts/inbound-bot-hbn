package main.modules.commands;

import main.Main;
import main.modules.permission.PermissionManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.util.List;

public class Mute extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Message message = event.getMessage();
        if (PermissionManager.canManageRoles(message.getMember())){
            if (message.getContentRaw().startsWith(Main.COMMAND_PREFIX + "mute")){
                List<Member> members = message.getMentionedMembers();
                try{
                    Role muteRole = event.getGuild().getRolesByName(Main.MUTE_ROLE, false).get(0);
                    for(Member member: members)
                        event.getGuild().addRoleToMember(member, muteRole).queue();
                    EmbedBuilder embed = new EmbedBuilder();
                    embed.setAuthor("muted the mentioned users <3", null, event.getJDA().getSelfUser().getAvatarUrl());
                    embed.setColor(Color.green);
                    message.getChannel().sendMessage(embed.build()).queue();
                }
                catch (IndexOutOfBoundsException e) {
                    EmbedBuilder embed = new EmbedBuilder();
                    embed.setAuthor("you don't have a role named \"" + Main.MUTE_ROLE + "\" on this server", null, event.getJDA().getSelfUser().getAvatarUrl());
                    embed.setColor(Color.red);
                    message.getChannel().sendMessage(embed.build()).queue();
                }
                catch (InsufficientPermissionException e){
                    EmbedBuilder embed = new EmbedBuilder();
                    embed.setAuthor("i don't have permissions to manage roles. you can turn those on in role settings <3", null, event.getJDA().getSelfUser().getAvatarUrl());
                    embed.setColor(Color.red);
                    message.getChannel().sendMessage(embed.build()).queue();
                }
                catch (HierarchyException e){
                    EmbedBuilder embed = new EmbedBuilder();
                    embed.setAuthor("i don't have permissions to mute. make sure that the mute role is not higher than my highest role", null, event.getJDA().getSelfUser().getAvatarUrl());
                    embed.setColor(Color.red);
                    message.getChannel().sendMessage(embed.build()).queue();
                }
            }
            else if (message.getContentRaw().startsWith(Main.COMMAND_PREFIX + "unmute")){
                List<Member> members = message.getMentionedMembers();
                try{
                    Role muteRole = event.getGuild().getRolesByName(Main.MUTE_ROLE, false).get(0);
                    for(Member member: members)
                        event.getGuild().removeRoleFromMember(member, muteRole).queue();
                    EmbedBuilder embed = new EmbedBuilder();
                    embed.setAuthor("un-muted the mentioned users <3", null, event.getJDA().getSelfUser().getAvatarUrl());
                    embed.setColor(Color.green);
                    message.getChannel().sendMessage(embed.build()).queue();
                }
                catch (IndexOutOfBoundsException e) {
                    EmbedBuilder embed = new EmbedBuilder();
                    embed.setAuthor("you don't have a role named \"" + Main.MUTE_ROLE + "\" on this server", null, event.getJDA().getSelfUser().getAvatarUrl());
                    embed.setColor(Color.red);
                    message.getChannel().sendMessage(embed.build()).queue();
                }
                catch (InsufficientPermissionException e){
                    EmbedBuilder embed = new EmbedBuilder();
                    embed.setAuthor("i don't have permissions to manage roles. you can turn those on in role settings <3", null, event.getJDA().getSelfUser().getAvatarUrl());
                    embed.setColor(Color.red);
                    message.getChannel().sendMessage(embed.build()).queue();
                }
                catch (HierarchyException e){
                    EmbedBuilder embed = new EmbedBuilder();
                    embed.setAuthor("i don't have permissions to unmute. make sure that the mute role is not higher than my highest role", null, event.getJDA().getSelfUser().getAvatarUrl());
                    embed.setColor(Color.red);
                    message.getChannel().sendMessage(embed.build()).queue();
                }
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
