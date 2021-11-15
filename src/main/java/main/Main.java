package main;

import main.modules.EnforceCapitalization;
import main.modules.HBNCommands;
import main.modules.chat.*;
import main.modules.commands.admin.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main extends ListenerAdapter {

    public static String COMMAND_PREFIX = "!";
    public static String MUTE_ROLE = "muted";

    public static JDA jda;

    private static Connection getConnection() throws URISyntaxException, SQLException {
        URI jdbUri = new URI(System.getenv("JAWSDB_MARIA_URL"));

        String username = jdbUri.getUserInfo().split(":")[0];
        String password = jdbUri.getUserInfo().split(":")[1];
        String port = String.valueOf(jdbUri.getPort());
        String jdbUrl = "jdbc:mysql://" + jdbUri.getHost() + ":" + port + jdbUri.getPath();

        return DriverManager.getConnection(jdbUrl, username, password);
    }

    public static void main(String[] args) throws LoginException {
        String token = "***REMOVED***";
        JDABuilder builder = JDABuilder.createDefault(token);builder.setToken(token);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.watching("your capitalization"));
        builder.addEventListeners(new HBNChatResponses());
        builder.addEventListeners(new ClearChat());
        builder.addEventListeners(new HBNCommands());
        builder.addEventListeners(new Trim());
        builder.addEventListeners(new Warn());
        builder.addEventListeners(new Mute());
        builder.addEventListeners(new EnforceCapitalization());

        jda = builder.build();
    }
}
