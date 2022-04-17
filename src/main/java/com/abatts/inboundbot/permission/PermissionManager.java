package com.abatts.inboundbot.permission;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;

public class PermissionManager {
    /**
     * Check if user has specified permission
     * @param permission
     * @param member
     * @return
     */
    public static boolean hasPermission(Permission permission, Member member){
        try{
            return member.hasPermission(permission);
        } catch (NullPointerException e){
            return false;
        }
    }

    public static boolean canManageMessages(Member member){
        return hasPermission(Permission.MESSAGE_MANAGE, member);
    }

    public static boolean canManageRoles(Member member){
        return hasPermission(Permission.MANAGE_ROLES, member);
    }

    public static boolean canMute(Member member){
        return canManageRoles(member);
    }

    public static boolean canWarn(Member member) { return canMute(member); }

    public static boolean isAdmin(Member member){
        return hasPermission(Permission.MANAGE_SERVER, member);
    }

    public static void throwIncorrectPermsWarning(GuildMessageReceivedEvent event, MessageChannel channel){
        EmbedBuilder embed = new EmbedBuilder()
                .setAuthor("You don't have right the permissions for that command", null, event.getJDA().getSelfUser().getAvatarUrl())
                .setColor(Color.red);
        channel.sendMessageEmbeds(embed.build()).queue();
    }

    public static class Music {
        public static boolean canManageMusic(Member member) {
            return canManageRoles(member) || member.getRoles().contains(member.getGuild().getRolesByName("dj", true).get(0));
        }

        public static void throwIncorrectPermsWarning(GuildMessageReceivedEvent event, MessageChannel channel){
            EmbedBuilder embed = new EmbedBuilder()
                    .setAuthor("You need to have a DJ role to run this command", null, event.getJDA().getSelfUser().getAvatarUrl())
                    .setColor(Color.red);
            channel.sendMessageEmbeds(embed.build()).queue();
        }
    }
}
