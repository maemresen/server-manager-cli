package com.maemresen.server.manager.cli.command.impl;

import com.google.inject.Inject;
import com.maemresen.server.manager.cli.command.AbstractCommand;
import com.maemresen.server.manager.cli.service.CommandService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.CommandLine;

@Slf4j
public class HelpCommand extends AbstractCommand {
  @Inject
  protected HelpCommand(CommandService commandService) {
    super("help", commandService);
  }

  @Override
  protected void handleCommandLine(CommandLine cmd) {
    log.info("Usage: help|status|up|down|history [-h]");
  }
}
