package com.maemresen.server.manager.cli.beans.command.impl;

import com.google.inject.Inject;
import com.maemresen.server.manager.cli.beans.command.AbstractCommand;
import com.maemresen.server.manager.cli.beans.service.CommandService;
import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.CommandLine;

@Slf4j
public class StatusCommand extends AbstractCommand {

  @Inject
  public StatusCommand(CommandService commandService) {
    super("status", commandService);
  }

  @Override
  protected void handleCommandLine(CommandLine cmd) throws SQLException {
    commandService.status();
  }
}
