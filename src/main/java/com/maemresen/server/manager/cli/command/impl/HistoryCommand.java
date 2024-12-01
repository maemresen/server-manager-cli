package com.maemresen.server.manager.cli.command.impl;

import com.google.inject.Inject;
import com.maemresen.server.manager.cli.command.AbstractCommand;
import com.maemresen.server.manager.cli.service.CommandService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.CommandLine;

@Slf4j
public class HistoryCommand extends AbstractCommand {

  @Inject
  public HistoryCommand(CommandService commandService) {
    super("history", commandService);
  }

  @Override
  protected void handleCommandLine(CommandLine cmd) throws InterruptedException {
    throw new UnsupportedOperationException("History command is not implemented yet.");
  }
}
