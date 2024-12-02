package com.maemresen.server.manager.cli.command.impl;

import com.google.inject.Inject;
import com.maemresen.server.manager.cli.command.AbstractCommand;
import com.maemresen.server.manager.cli.model.dto.SearchHistoryDto;
import com.maemresen.server.manager.cli.model.dto.Sort;
import com.maemresen.server.manager.cli.model.entity.Status;
import com.maemresen.server.manager.cli.service.CommandService;
import com.maemresen.server.manager.cli.utils.CmdUtils;
import com.maemresen.server.manager.cli.utils.DateTimeUtils;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

@Slf4j
public class HistoryCommand extends AbstractCommand {

  public static final String PARAMETER_FROM = "f";
  public static final String PARAMETER_TO = "t";
  public static final String PARAMETER_SORT = "o";
  public static final String PARAMETER_STATUS = "s";

  @Inject
  public HistoryCommand(CommandService commandService) {
    super("searchHistory", commandService);
  }

  @Override
  protected void configureOptions(Options options) {
    options.addOption(PARAMETER_FROM, "from", true, "yyyy-mm-dd");
    options.addOption(PARAMETER_TO, "to", true, "yyyy-mm-dd");
    options.addOption(PARAMETER_SORT, "sort", true, "asc|desc");
    options.addOption(
        PARAMETER_STATUS,
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

    CmdUtils.getParameter(cmd, PARAMETER_FROM, DateTimeUtils::parseDate)
        .ifPresent(searchHistoryDto::setFromDate);
    CmdUtils.getParameter(cmd, PARAMETER_TO, DateTimeUtils::parseDate)
        .ifPresent(searchHistoryDto::setToDate);
    CmdUtils.getEnumParameter(cmd, PARAMETER_SORT, Sort.class)
        .ifPresentOrElse(searchHistoryDto::setSort, () -> searchHistoryDto.setSort(Sort.DESC));
    CmdUtils.getEnumParameter(cmd, PARAMETER_STATUS, Status.class)
        .ifPresent(searchHistoryDto::setStatus);

    commandService.searchHistory(searchHistoryDto);
  }
}
