package com.maemresen.server.manager.cli.command.impl;

import com.google.inject.Inject;
import com.maemresen.server.manager.cli.command.AbstractCommand;
import com.maemresen.server.manager.cli.service.CommandService;
import com.maemresen.server.manager.cli.utils.CmdUtils;
import com.maemresen.server.manager.cli.utils.ExecutionPauseUtils;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
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
  protected void configureOptions(Options options) {
    options.addOption("b", "before", true, "before the command");
  }

  @Override
  protected void handleCommandLine(CommandLine cmd) throws InterruptedException, SQLException {
    commandService.up();
    Optional<LocalDateTime> before = CmdUtils.getDateTimeOption(cmd, "b");
    if (before.isPresent()) {
      waitAndDownAfter(before.get());
    }
  }

  private void waitAndDownAfter(LocalDateTime before) throws InterruptedException, SQLException {
    Duration durationToWaitDown = Duration.between(LocalDateTime.now(), before);
    if (durationToWaitDown.isPositive()) {
      ExecutionPauseUtils.delay(durationToWaitDown);
    }
    commandService.down();
  }
}
