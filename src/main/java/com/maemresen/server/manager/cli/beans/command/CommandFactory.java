package com.maemresen.server.manager.cli.beans.command;

import com.google.inject.Inject;
import com.maemresen.server.manager.cli.beans.command.impl.HelpCommand;
import java.util.Set;

public class CommandFactory {

  private final Set<Command> commands;
  private final HelpCommand helpCommand;

  @Inject
  public CommandFactory(Set<Command> commands, HelpCommand helpCommand) {
    this.commands = commands;
    this.helpCommand = helpCommand;
  }

  public Command getCommand(String commandName) {
    return commands.stream()
        .filter(x -> x.getName().equals(commandName))
        .findFirst()
        .orElse(helpCommand);
  }
}
