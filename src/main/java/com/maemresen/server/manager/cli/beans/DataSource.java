package com.maemresen.server.manager.cli.beans;

import com.google.inject.Inject;
import com.maemresen.server.manager.cli.utils.properties.DbProps;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DataSource {

  private final String jdbcUrl;
  private final String username;
  private final String password;

  @Inject
  public DataSource(AppProps appProps) {
    this.jdbcUrl = appProps.getProp(DbProps.JDBC_URL).orElseThrow();
    this.username = appProps.getProp(DbProps.JDBC_USERNAME).orElseThrow();
    this.password = appProps.getProp(DbProps.JDBC_PASSWORD).orElse(null);
  }

  public Connection createConnection() throws SQLException {
    return DriverManager.getConnection(jdbcUrl, username, password);
  }

  public void executeFile(String filePath) throws SQLException, IOException {
    try (var connection = createConnection();
        Statement statement = connection.createStatement();
        InputStream inputStream = getClass().getResourceAsStream(filePath);
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
}
