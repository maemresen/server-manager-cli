package com.maemresen.server.manager.cli;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.maemresen.server.manager.cli.beans.DataSource;
import com.maemresen.server.manager.cli.beans.command.CommandFactory;
import com.maemresen.server.manager.cli.beans.command.impl.HelpCommand;
import com.maemresen.server.manager.cli.config.ApplicationModule;
import com.maemresen.server.manager.cli.exception.CommandNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServerManagerCli {
  public static void main(String[] args) throws InterruptedException, SQLException, IOException {
    Injector injector = Guice.createInjector(new ApplicationModule());

    injector.getInstance(DataSource.class).executeFile("/scheme.sql");

    try {
      String commandName = args[0];
      String[] commandParameters = Arrays.copyOfRange(args, 1, args.length);
      CommandFactory factory = injector.getInstance(CommandFactory.class);
      factory.getCommand(commandName).run(commandParameters);
    } catch (CommandNotFoundException exception) {
      log.info(exception.getMessage());
      injector.getInstance(HelpCommand.class).run();
    }
  }
}
