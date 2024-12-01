package com.maemresen.server.manager.cli.command.impl;

import com.google.inject.Inject;
import com.maemresen.server.manager.cli.command.AbstractCommand;
import com.maemresen.server.manager.cli.service.CommandService;
import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.CommandLine;

@Slf4j
public class DownCommand extends AbstractCommand {

  @Inject
  public DownCommand(CommandService commandService) {
    super("down", commandService);
  }

  @Override
  protected void handleCommandLine(CommandLine cmd) throws InterruptedException, SQLException {
    commandService.down();
  }
}
