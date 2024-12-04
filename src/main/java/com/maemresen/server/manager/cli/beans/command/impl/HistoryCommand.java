package com.maemresen.server.manager.cli.beans.command.impl;

import com.google.inject.Inject;
import com.maemresen.server.manager.cli.beans.command.AbstractCommand;
import com.maemresen.server.manager.cli.beans.service.CommandService;
import com.maemresen.server.manager.cli.model.dto.SearchHistoryDto;
import com.maemresen.server.manager.cli.model.dto.Sort;
import com.maemresen.server.manager.cli.model.entity.Status;
import com.maemresen.server.manager.cli.utils.CmdUtils;
import com.maemresen.server.manager.cli.utils.DateTimeUtils;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

@Slf4j
public class HistoryCommand extends AbstractCommand {

  public static final String PARAMETER_FROM = "f";
  public static final String PARAMETER_TO = "t";
  public static final String PARAMETER_SORT = "o";
  public static final String PARAMETER_STATUS = "s";

  @Inject
  public HistoryCommand(CommandService commandService) {
    super("history", commandService);
  }

  @Override
  protected void configureOptions(Options options) {
    options.addOption(
        PARAMETER_FROM, "from", true, "yyyy-mm-dd : Filter events starting from this date.");
    options.addOption(PARAMETER_TO, "to", true, "yyyy-mm-dd : Filter events up to this date.");
    options.addOption(
        PARAMETER_SORT,
        "sort",
        true,
        "asc|desc : Sort events in ascending or descending order based on creation timve.");
    options.addOption(
        PARAMETER_STATUS,
        "status",
        true,
        Arrays.stream(Status.values())
                .map(Enum::name)
                .map(String::toLowerCase)
                .collect(Collectors.joining("|"))
            + " : Filter by specific server statuses.");
  }

  @Override
  protected void handleCommandLine(CommandLine cmd) throws SQLException, ParseException {
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
