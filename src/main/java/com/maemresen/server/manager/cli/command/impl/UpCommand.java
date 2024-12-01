package com.maemresen.server.manager.cli.command.impl;

import com.google.inject.Inject;
import com.maemresen.server.manager.cli.command.AbstractCommand;
import com.maemresen.server.manager.cli.service.CommandService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

@Slf4j
public class UpCommand extends AbstractCommand {
  @Inject
  public UpCommand(CommandService commandService) {
    super("up", commandService);
  }

  @Override
  protected Options createOptions() {
    Options options = new Options();
    options.addOption("b", "before", true, "before the command");
    return options;
  }

  @Override
  protected void handleCommandLine(CommandLine cmd) throws InterruptedException {
    throw new UnsupportedOperationException("Up command is not implemented yet.");
  }
}
