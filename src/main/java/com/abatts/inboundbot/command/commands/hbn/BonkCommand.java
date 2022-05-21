package com.abatts.inboundbot.command.commands.hbn;

import com.abatts.inboundbot.Bot;
import com.abatts.inboundbot.command.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class BonkCommand implements Command {
    private final String name = "bonk";
    private final String BONK_IMAGE = "https://media.giphy.com/media/RodyInaeK9W3EZNaoB/giphy.gif";

    @Override
    public void runCommand(GuildMessageReceivedEvent event) {
        Message message = event.getMessage();

        String bonkString = message.getMember().getEffectiveName() + " bonked ";
        if (message.getMentionedUsers().size() > 0)
            bonkString += message.getMentionedMembers().get(0).getEffectiveName();
        else
            bonkString += message.getContentRaw().substring(Bot.DEFAULT_PREFIX.length() + name.length() + 1);

        EmbedBuilder bonkEmbed = new EmbedBuilder()
                .setAuthor("BONK!", null, event.getJDA().getSelfUser().getAvatarUrl())
                .setTitle(bonkString)
                .setImage(BONK_IMAGE);
        message.getChannel().sendMessageEmbeds(bonkEmbed.build()).queue();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return "Bonk another user \n`bonk <user>`";
    }
}
