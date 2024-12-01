package com.maemresen.server.manager.cli.command;

import com.maemresen.server.manager.cli.command.impl.DownCommand;
import com.maemresen.server.manager.cli.command.impl.HistoryCommand;
import com.maemresen.server.manager.cli.command.impl.StatusCommand;
import com.maemresen.server.manager.cli.command.impl.UpCommand;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CommandFactory {

  public static Command getCommand(String commandName) {
    return switch (commandName) {
      case "status" -> new StatusCommand();
      case "up" -> new UpCommand();
      case "down" -> new DownCommand();
      case "history" -> new HistoryCommand();
      default -> throw new IllegalArgumentException("Unknown command: " + commandName);
    };
  }
}
