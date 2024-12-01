package com.maemresen.server.manager.cli.command.impl;

import com.maemresen.server.manager.cli.command.AbstractCommand;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.CommandLine;

@Slf4j
public class HistoryCommand extends AbstractCommand {
  public HistoryCommand() {
    super("history");
  }

  @Override
  protected void handleCommandLine(CommandLine cmd) throws InterruptedException {
    throw new UnsupportedOperationException("History command is not implemented yet.");
  }
}
