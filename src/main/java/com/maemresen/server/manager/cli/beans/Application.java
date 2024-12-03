package com.maemresen.server.manager.cli.beans;

import com.google.inject.Inject;
import com.maemresen.server.manager.cli.beans.command.CommandFactory;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Application {
  private final DataSource dataSource;
  private final CommandFactory commandFactory;

  @Inject
  public Application(DataSource dataSource, CommandFactory commandFactory) {
    this.dataSource = dataSource;
    this.commandFactory = commandFactory;
  }

  public void run(String... args) throws SQLException, IOException, InterruptedException {
    dataSource.executeFile("/scheme.sql");
    String commandName = args[0];
    String[] commandParameters = Arrays.copyOfRange(args, 1, args.length);
    commandFactory.getCommand(commandName).run(commandParameters);
  }
}
