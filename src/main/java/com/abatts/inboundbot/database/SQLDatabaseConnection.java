package com.abatts.inboundbot.database;

import com.abatts.inboundbot.Bot;

import java.sql.*;

public class SQLDatabaseConnection {

    static {
        try (PreparedStatement statement = getConnection().prepareStatement(""" 
                CREATE TABLE IF NOT EXISTS GUILD_SETTINGS
                (
                    guild_id VARCHAR(20) NOT NULL,
                    command_prefix VARCHAR(255) DEFAULT ?,
                    mute_role VARCHAR(255) DEFAULT ?,
                    dj_role VARCHAR(255) DEFAULT ?
                )
        """)) {
            statement.setString(1, Bot.DEFAULT_PREFIX);
            statement.setString(2, Bot.DEFAULT_MUTE_ROLE);
            statement.setString(3, Bot.DEFAULT_DJ_ROLE);
            statement.execute();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException{
        return DriverManager.getConnection(
                "jdbc:" + Bot.getEnvProperty("DB_CONNECTION_STRING"),
                Bot.getEnvProperty("DB_USER"),
                Bot.getEnvProperty("DB_PASSWORD")
        );
    }
}
