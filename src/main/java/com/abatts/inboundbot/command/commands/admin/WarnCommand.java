package com.abatts.inboundbot.command.commands.admin;

import com.abatts.inboundbot.Bot;
import com.abatts.inboundbot.command.Command;
import com.abatts.inboundbot.permission.PermissionManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class WarnCommand implements Command {
    public WarnCommand(){
        try(PreparedStatement statement = Bot.connection.prepareStatement("""
            CREATE TABLE IF NOT EXISTS WARNS
            (
                id INT PRIMARY KEY AUTO_INCREMENT,
                guild_id VARCHAR(20) NOT NULL,
                user_id VARCHAR(20) NOT NULL,
                moderator_id VARCHAR(20) NOT NULL,
                message VARCHAR(200),
                created_at DATETIME DEFAULT CURRENT_TIMESTAMP
            )
            """)){
            statement.executeUpdate();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void runCommand(GuildMessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();

        if (PermissionManager.canWarn(event.getMember())){
            List<Member> members = event.getMessage().getMentionedMembers();

            if (members.isEmpty()){
                EmbedBuilder embed = new EmbedBuilder()
                        .setAuthor("You need to mention the user(s) you want to warn", null, event.getJDA().getSelfUser().getAvatarUrl())
                        .setColor(Color.red);
                event.getChannel().sendMessageEmbeds(embed.build()).queue();
                return;
            }

            String[] args = message.split(" ");
            String warnMessage = "";
            try{
                warnMessage = message.substring(message.indexOf(args[members.size() + 1]));
            }
            catch(IndexOutOfBoundsException ignored){}

            if (warnMessage.length() > 199)
                warnMessage = warnMessage.substring(0, 198);

            String warnReason = warnMessage.equals("") ? "`No reason provided`" : "`" + warnMessage + "`";
            EmbedBuilder warning = new EmbedBuilder()
                    .setAuthor("You have received a warning in " + event.getGuild().getName(), null, event.getChannel().getJDA().getSelfUser().getAvatarUrl())
                    .setDescription("Reason: " + warnReason
                            +"\n\n Warned by: " + event.getAuthor().getAsMention())
                    .setColor(Color.yellow);

            for (Member member : members){
                int warnsCount = 1;
                try(PreparedStatement st = Bot.connection.prepareStatement("""
                        SELECT *
                        FROM WARNS
                        WHERE guild_id = ? AND user_id = ?
                    """)){
                    st.setString(1, event.getGuild().getId());
                    st.setString(2, member.getId());
                    ResultSet rs = st.executeQuery();

                    while (rs.next())
                        warnsCount++;
                }
                catch(SQLException e){
                    e.printStackTrace();
                }

                try(PreparedStatement st1 = Bot.connection.prepareStatement("""
                        INSERT INTO WARNS(guild_id, user_id, moderator_id, message)
                        VALUES(?, ?, ?, ?)
                    """)){
                    st1.setString(1, event.getGuild().getId());
                    st1.setString(2, member.getId());
                    st1.setString(3, event.getAuthor().getId());
                    st1.setString(4, warnMessage);
                    st1.executeUpdate();

                    if (! member.getUser().isBot())
                        member.getUser().openPrivateChannel().flatMap(channel -> channel.sendMessageEmbeds(warning.build())).queue();
                    EmbedBuilder embed = new EmbedBuilder()
                            .setAuthor(member.getEffectiveName() + " received a warning", null, event.getJDA().getSelfUser().getAvatarUrl())
                            .setDescription("Reason: " + warnReason
                                    +"\n\n Warned by: " + event.getAuthor().getAsMention())
                            .setFooter("This is their " + getOrdinal(warnsCount) + " warning")
                            .setColor(Color.yellow);
                    event.getChannel().sendMessageEmbeds(embed.build()).queue();
                }
                catch(SQLException e){
                    e.printStackTrace();
                }
            }
        }
        else
            PermissionManager.throwIncorrectPermsWarning(event, event.getChannel());
    }

    @Override
    public String getName() {
        return "warn";
    }

    @Override
    public String getDescription() {
        return "Warn a specified user or group of users \n`warn <user(s) to warn> <warning message>`";
    }

    public String getOrdinal(int i){
        String[] endings = new String[]{"th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th"};
        return switch (i % 100) {
            case 11, 12, 13 -> i + "th";
            default -> i + endings[i % 10];
        };
    }
}
