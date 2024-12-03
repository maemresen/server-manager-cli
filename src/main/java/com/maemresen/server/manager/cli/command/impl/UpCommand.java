package com.maemresen.server.manager.cli.command.impl;

import com.google.inject.Inject;
import com.maemresen.server.manager.cli.command.AbstractCommand;
import com.maemresen.server.manager.cli.service.CommandService;
import com.maemresen.server.manager.cli.utils.CmdUtils;
import com.maemresen.server.manager.cli.utils.DateTimeUtils;
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
  public static final String PARAMETER_BEFORE = "b";

  @Inject
  public UpCommand(CommandService commandService) {
    super("up", commandService);
  }

  @Override
  protected void configureOptions(Options options) {
    options.addOption(
        PARAMETER_BEFORE,
        "before",
        true,
        "yyyy-mm-dd hh:mm (in UTC) Schedules a server stop at the given timestamp.");
  }

  @Override
  protected void handleCommandLine(CommandLine cmd) throws InterruptedException, SQLException {
    commandService.up();
    Optional<LocalDateTime> before =
        CmdUtils.getParameter(cmd, PARAMETER_BEFORE, DateTimeUtils::parseDateTimeWithoutSeconds);
    if (before.isPresent()) {
      waitAndDownAfter(before.get());
    }
  }

  private void waitAndDownAfter(LocalDateTime before) throws InterruptedException, SQLException {
    Duration durationToWaitDown = Duration.between(DateTimeUtils.now(), before);
    if (durationToWaitDown.isPositive()) {
      ExecutionPauseUtils.delay(durationToWaitDown);
    }
    commandService.down();
  }
}
