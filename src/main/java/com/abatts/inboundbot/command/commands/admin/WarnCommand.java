package com.abatts.inboundbot.command.commands.admin;

import com.abatts.inboundbot.command.Command;
import com.abatts.inboundbot.permission.PermissionManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.List;

public class WarnCommand implements Command {

    @Override
    public void runCommand(GuildMessageReceivedEvent event) {
        Message message = event.getMessage();

        if (PermissionManager.canWarn(message.getMember())){
            List<Member> members = message.getMentionedMembers();

            if (members.isEmpty()){
                EmbedBuilder embed = new EmbedBuilder()
                        .setAuthor("You need to mention the user(s) you want to warn", null, event.getJDA().getSelfUser().getAvatarUrl())
                        .setColor(Color.red);
                message.getChannel().sendMessageEmbeds(embed.build()).queue();
                return;
            }

            String[] split = message.getContentRaw().split("\\s+");
            StringBuilder warnMessage = new StringBuilder();
            for (int i = members.size() + 1; i < split.length; i ++ )
                warnMessage.append(split[i]).append(" ");

            EmbedBuilder warning = new EmbedBuilder()
                    .setAuthor("You have received a warning in " + message.getGuild().getName(), null, event.getChannel().getJDA().getSelfUser().getAvatarUrl())
                    .setColor(Color.yellow);
            if (split.length > members.size() + 1)
                warning.setDescription("from " + message.getAuthor().getAsMention() + " : " + warnMessage);

            for (Member member : members)
                member.getUser().openPrivateChannel().flatMap(channel -> channel.sendMessageEmbeds(warning.build())).queue();
            EmbedBuilder embed = new EmbedBuilder()
                    .setAuthor("warned the mentioned users", null, event.getJDA().getSelfUser().getAvatarUrl())
                    .setColor(Color.yellow);
            message.getChannel().sendMessageEmbeds(embed.build()).queue();
        }
        else
            PermissionManager.throwIncorrectPermsWarning(event, message.getChannel());
    }

    @Override
    public String getName() {
        String name = "warn";
        return name;
    }

    @Override
    public String getDescription() {
        return "Warn a specified user or group of users \n`mute <user(s) to warn> <warning message>`";
    }
}
