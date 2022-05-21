package com.abatts.inboundbot.command.commands;

import com.abatts.inboundbot.command.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.List;

public class GetAvatarCommand implements Command {
    @Override
    public void runCommand(GuildMessageReceivedEvent event) {
        Message message = event.getMessage();

        List<Member> members = message.getMentionedMembers();
        if (members.isEmpty()){
            EmbedBuilder embed = new EmbedBuilder()
                    .setAuthor("You need to mention the user you want the avatar of", null, event.getJDA().getSelfUser().getAvatarUrl())
                    .setColor(Color.red);
            message.getChannel().sendMessageEmbeds(embed.build()).queue();
            return;
        }
        Member selectedMember = members.get(0);

        String userName = selectedMember.getNickname();
        if (userName == null)
            userName = selectedMember.getUser().getName();

        EmbedBuilder embed = new EmbedBuilder()
                .setAuthor(userName + "'s profile picture", null, event.getJDA().getSelfUser().getAvatarUrl())
                .setImage(selectedMember.getUser().getAvatarUrl());
        message.getChannel().sendMessageEmbeds(embed.build()).queue();

    }

    @Override
    public String getName() {
        return "avatar";
    }

    @Override
    public String getDescription() {
        return "Returns the avatar of a mentioned user \n`avatar <user>`";
    }
}
