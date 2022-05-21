package com.abatts.inboundbot.command.commands.admin;

import com.abatts.inboundbot.Bot;
import com.abatts.inboundbot.command.Command;
import com.abatts.inboundbot.permission.PermissionManager;
import com.jagrosh.jdautilities.menu.Paginator;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.PermissionException;

import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ViewWarnsCommand implements Command {
    private final Paginator.Builder warnEmbed = new Paginator.Builder()
            .setColumns(1)
            .setFinalAction(m -> {try{m.clearReactions().queue();}catch(PermissionException ignore){}})
            .setItemsPerPage(15)
            .waitOnSinglePage(false)
            .useNumberedItems(true)
            .showPageNumbers(true)
            .wrapPageEnds(true)
            .setEventWaiter(Bot.getEventWaiter())
            .setTimeout(1, TimeUnit.MINUTES);

    @Override
    public void runCommand(GuildMessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();

        if (PermissionManager.canWarn(event.getMember())) {
            List<Member> members = event.getMessage().getMentionedMembers();

            if (members.isEmpty()){
                EmbedBuilder embed = new EmbedBuilder()
                        .setAuthor("You need to mention the user to display warnings for", null, event.getJDA().getSelfUser().getAvatarUrl())
                        .setColor(Color.red);
                event.getChannel().sendMessageEmbeds(embed.build()).queue();
                return;
            }

            Member selectedMember = members.get(0);

            try(PreparedStatement st = Bot.connection.prepareStatement("""
                    SELECT *
                    FROM WARNS
                    WHERE guild_id = ? AND user_id = ?
                """)){
                st.setString(1, event.getGuild().getId());
                st.setString(2, selectedMember.getId());
                ResultSet rs = st.executeQuery();

                ArrayList<String> warns = new ArrayList<>();
                while(rs.next()){
                    String warnReason = rs.getString("message").equals("") ? "`No reason provided`" : "`" + rs.getString("message") + "`";
                    Timestamp ts = rs.getTimestamp("created_at");
                    User moderator = event.getJDA().retrieveUserById(rs.getString("moderator_id")).complete();
                    warns.add("Warn Case #" + rs.getInt("id") +
                            "\nWarned: <t:" + ts.getTime() + ">" +
                            "\nBy: " + moderator.getAsMention() +
                            "\nReason: " + warnReason);
                }

                if (warns.isEmpty()){
                    EmbedBuilder embed = new EmbedBuilder()
                            .setAuthor("That user hasn't received any warnings (yet)", null, event.getJDA().getSelfUser().getAvatarUrl());
                    event.getChannel().sendMessageEmbeds(embed.build()).queue();
                    return;
                }
                warnEmbed.setText("Warnings for **" + selectedMember.getEffectiveName() + "** in **"+ event.getGuild().getName() + "**:").setItems(warns.toArray(new String[0]));
                warnEmbed.build().display(event.getMessage().getChannel());

            } catch (SQLException e) {
                e.printStackTrace();
            }


        }
        else
            PermissionManager.throwIncorrectPermsWarning(event, event.getChannel());
    }

    @Override
    public String getName() {
        return "warns";
    }

    @Override
    public String getDescription() {
        return "View the warnings a specific user has received \n`warns <user>`";
    }
}
