package com.maemresen.server.manager.cli;

import com.maemresen.server.manager.cli.utils.EnvUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DbConnection {

  private static final String DEFAULT_JDBC = "jdbc:h2:file:./data/server-status;AUTO_SERVER=TRUE";
  private static final String DEFAULT_DB_USER = "sa";
  private static final String DEFAULT_DB_PASS = "";

  private static final String JDBC = EnvUtils.getEnv("JDBC", DEFAULT_JDBC);
  private static final String DB_USERNAME = EnvUtils.getEnv("DB_USER", DEFAULT_DB_USER);
  private static final String DB_PASSWORD = EnvUtils.getEnv("DB_PASS", DEFAULT_DB_PASS);

  public static Connection getConnection() throws SQLException {
    return createConnection();
  }

  public static void executeFile(String filePath) throws SQLException, IOException {
    try (var connection = createConnection();
        Statement statement = connection.createStatement();
        InputStream inputStream = DbConnection.class.getResourceAsStream(filePath);
        InputStreamReader is = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(is)) {
      StringBuilder sql = new StringBuilder();
      String line;
      while ((line = reader.readLine()) != null) {
        sql.append(line).append(System.lineSeparator());
        // Execute when we hit the end of a statement
        if (line.trim().endsWith(";")) {
          statement.execute(sql.toString());
          sql.setLength(0); // Clear the StringBuilder
        }
      }
    }
  }

  private static Connection createConnection() throws SQLException {
    return DriverManager.getConnection(JDBC, DB_USERNAME, DB_PASSWORD);
  }
}
