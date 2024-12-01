package com.maemresen.server.manager.cli.command;

import com.google.inject.Inject;
import java.util.Set;

public class CommandFactory {

  private final Set<Command> commands;

  @Inject
  public CommandFactory(Set<Command> commands) {
    this.commands = commands;
  }

  public Command getCommand(String commandName) {
    return commands.stream()
        .filter(x -> x.getName().equals(commandName))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Unknown command: " + commandName));
  }
}
