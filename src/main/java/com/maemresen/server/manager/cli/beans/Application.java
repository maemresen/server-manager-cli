package com.maemresen.server.manager.cli.beans;

import com.google.inject.Inject;
import com.maemresen.server.manager.cli.beans.command.CommandFactory;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Application {
  private final CommandFactory commandFactory;

  @Inject
  public Application(CommandFactory commandFactory) {
    this.commandFactory = commandFactory;
  }

  public void run(String... args) throws InterruptedException {
    String commandName = args[0];
    String[] commandParameters = Arrays.copyOfRange(args, 1, args.length);
    commandFactory.getCommand(commandName).run(commandParameters);
  }
}
