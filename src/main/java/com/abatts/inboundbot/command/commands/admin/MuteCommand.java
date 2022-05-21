package com.abatts.inboundbot.command.commands.admin;

import com.abatts.inboundbot.Bot;
import com.abatts.inboundbot.command.Command;
import com.abatts.inboundbot.permission.PermissionManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;

import java.awt.*;
import java.util.List;

public class MuteCommand implements Command {

    @Override
    public void runCommand(GuildMessageReceivedEvent event) {
        Message message = event.getMessage();

        if (PermissionManager.canManageRoles(message.getMember())) {
            List<Member> members = message.getMentionedMembers();
            try {
                Role muteRole = event.getGuild().getRolesByName(Bot.DEFAULT_MUTE_ROLE, true).get(0);
                for (Member member : members){
                    if (! member.getUser().isBot())
                        event.getGuild().addRoleToMember(member, muteRole).queue();
                    else{
                        if (members.size() == 1){
                            EmbedBuilder embed = new EmbedBuilder()
                                    .setAuthor("I can't mute myself", null, event.getJDA().getSelfUser().getAvatarUrl())
                                    .setColor(Color.red);
                            message.getChannel().sendMessageEmbeds(embed.build()).queue();
                            return;
                        }
                    }
                }

                EmbedBuilder embed = new EmbedBuilder()
                        .setAuthor("Successfully muted the mentioned members", null, event.getJDA().getSelfUser().getAvatarUrl())
                        .setColor(Color.green);
                message.getChannel().sendMessageEmbeds(embed.build()).queue();
            } catch (IndexOutOfBoundsException e) {
                EmbedBuilder embed = new EmbedBuilder()
                        .setAuthor("You don't have a role named `\"" + Bot.DEFAULT_MUTE_ROLE + "\"`.", null, event.getJDA().getSelfUser().getAvatarUrl())
                        .setColor(Color.red);
                message.getChannel().sendMessageEmbeds(embed.build()).queue();
            } catch (InsufficientPermissionException e) {
                EmbedBuilder embed = new EmbedBuilder()
                        .setAuthor("I don't have have `Manage Messages` permissions so I can't run this command", null, event.getJDA().getSelfUser().getAvatarUrl())
                        .setColor(Color.red);
                message.getChannel().sendMessageEmbeds(embed.build()).queue();
            } catch (HierarchyException e) {
                EmbedBuilder embed = new EmbedBuilder()
                        .setAuthor("I don't have permissions to muter users. Make sure that the mute role is not higher than my highest role", null, event.getJDA().getSelfUser().getAvatarUrl())
                        .setColor(Color.red);
                message.getChannel().sendMessageEmbeds(embed.build()).queue();
            }
        }
        else
            PermissionManager.throwIncorrectPermsWarning(event, message.getChannel());
    }

    @Override
    public String getName() {
        String name = "mute";
        return name;
    }

    @Override
    public String getDescription() {
        return "Mute a specified user or group of users \n`mute <user(s) to mute>`";
    }
}
