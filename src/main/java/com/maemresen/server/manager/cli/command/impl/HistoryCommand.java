package com.maemresen.server.manager.cli.command.impl;

import com.google.inject.Inject;
import com.maemresen.server.manager.cli.command.AbstractCommand;
import com.maemresen.server.manager.cli.model.dto.SearchHistoryDto;
import com.maemresen.server.manager.cli.model.dto.Sort;
import com.maemresen.server.manager.cli.model.entity.Status;
import com.maemresen.server.manager.cli.service.CommandService;
import com.maemresen.server.manager.cli.utils.CmdUtils;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

@Slf4j
public class HistoryCommand extends AbstractCommand {

  @Inject
  public HistoryCommand(CommandService commandService) {
    super("history", commandService);
  }

  @Override
  protected void configureOptions(Options options) {
    options.addOption("f", "from", true, "yyyy-mm-dd");
    options.addOption("t", "to", true, "yyyy-mm-dd");
    options.addOption("o", "sort", true, "asc|desc");
    options.addOption(
        "s",
        "status",
        true,
        Arrays.stream(Status.values())
            .map(Enum::name)
            .map(String::toLowerCase)
            .collect(Collectors.joining("|")));
  }

  @Override
  protected void handleCommandLine(CommandLine cmd) throws SQLException {
    SearchHistoryDto searchHistoryDto = new SearchHistoryDto();

    CmdUtils.getDateOption(cmd, "f").ifPresent(searchHistoryDto::setFromDate);
    CmdUtils.getDateOption(cmd, "t").ifPresent(searchHistoryDto::setToDate);
    CmdUtils.getEnumOption(cmd, "o", Sort.class)
        .ifPresentOrElse(searchHistoryDto::setSort, () -> searchHistoryDto.setSort(Sort.DESC));
    CmdUtils.getEnumOption(cmd, "s", Status.class).ifPresent(searchHistoryDto::setStatus);

    commandService.history(searchHistoryDto);
  }
}
