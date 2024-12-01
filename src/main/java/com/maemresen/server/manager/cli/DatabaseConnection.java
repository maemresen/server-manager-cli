package com.maemresen.server.manager.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DatabaseConnection {
  private static final String JDBC_URL = "jdbc:h2:file:./data/server-status;AUTO_SERVER=TRUE";
  private static final String USERNAME = "sa";
  private static final String PASSWORD = "";

  private static DatabaseConnection INSTANCE;

  public static DatabaseConnection getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new DatabaseConnection();
    }
    return INSTANCE;
  }

  public Connection getConnection() throws SQLException {
    return DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
  }

  public void executeFile(String filePath) throws SQLException, IOException {
    try (var connection = getConnection();
        var statement = connection.createStatement();
        var inputStream = DatabaseConnection.class.getResourceAsStream(filePath);
        var is = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        var reader = new BufferedReader(is)) {
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
}
