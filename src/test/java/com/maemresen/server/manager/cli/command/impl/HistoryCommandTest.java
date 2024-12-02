package com.maemresen.server.manager.cli.command.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.maemresen.server.manager.cli.model.dto.SearchHistoryDto;
import com.maemresen.server.manager.cli.model.dto.Sort;
import com.maemresen.server.manager.cli.model.entity.Status;
import com.maemresen.server.manager.cli.service.CommandService;
import com.maemresen.server.manager.cli.utils.DateTimeUtils;
import java.sql.SQLException;
import org.apache.commons.cli.CommandLine;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class HistoryCommandTest {

  private static final String DATE_STRING = "2024-12-25";

  @Mock private CommandLine mockCmd;

  @Mock private CommandService commandService;

  @InjectMocks private HistoryCommand command;

  @Captor private ArgumentCaptor<SearchHistoryDto> dtoArgumentCaptor;

  private void doNothingWhenSearchHistory() throws SQLException {
    doNothing().when(commandService).searchHistory(dtoArgumentCaptor.capture());
  }

  private void whenCmdGetParameterValue(String parameter, String value) {
    when(mockCmd.hasOption(parameter)).thenReturn(true);
    when(mockCmd.getOptionValue(parameter)).thenReturn(value);
  }

  private void whenCmdGetParameterValue(String parameter) {
    when(mockCmd.hasOption(parameter)).thenReturn(false);
  }

  @Test
  void shouldRunCommand() throws SQLException {
    doNothingWhenSearchHistory();

    command.handleCommandLine(mockCmd);

    verify(commandService).searchHistory(dtoArgumentCaptor.capture());
    verifyNoMoreInteractions(commandService);
  }

  @Test
  void shouldSetFromParameter() throws SQLException {
    doNothingWhenSearchHistory();
    whenCmdGetParameterValue(HistoryCommand.PARAMETER_FROM, DATE_STRING);
    whenCmdGetParameterValue(HistoryCommand.PARAMETER_TO);
    whenCmdGetParameterValue(HistoryCommand.PARAMETER_SORT);
    whenCmdGetParameterValue(HistoryCommand.PARAMETER_STATUS);

    command.handleCommandLine(mockCmd);

    verify(commandService).searchHistory(dtoArgumentCaptor.capture());
    verifyNoMoreInteractions(commandService);

    assertThat(dtoArgumentCaptor.getValue())
        .extracting(SearchHistoryDto::getFromDate)
        .isEqualTo(DateTimeUtils.parseDate(DATE_STRING));
  }

  @Test
  void shouldSetToParameter() throws SQLException {
    doNothingWhenSearchHistory();
    whenCmdGetParameterValue(HistoryCommand.PARAMETER_FROM);
    whenCmdGetParameterValue(HistoryCommand.PARAMETER_TO, DATE_STRING);
    whenCmdGetParameterValue(HistoryCommand.PARAMETER_SORT);
    whenCmdGetParameterValue(HistoryCommand.PARAMETER_STATUS);

    command.handleCommandLine(mockCmd);

    verify(commandService).searchHistory(dtoArgumentCaptor.capture());
    verifyNoMoreInteractions(commandService);

    assertThat(dtoArgumentCaptor.getValue())
        .extracting(SearchHistoryDto::getToDate)
        .isEqualTo(DateTimeUtils.parseDate(DATE_STRING));
  }

  @ParameterizedTest
  @EnumSource(Sort.class)
  void shouldSetSortParameter(Sort sort) throws SQLException {
    doNothingWhenSearchHistory();
    whenCmdGetParameterValue(HistoryCommand.PARAMETER_FROM);
    whenCmdGetParameterValue(HistoryCommand.PARAMETER_TO);
    whenCmdGetParameterValue(HistoryCommand.PARAMETER_SORT, sort.name());
    whenCmdGetParameterValue(HistoryCommand.PARAMETER_STATUS);

    command.handleCommandLine(mockCmd);

    verify(commandService).searchHistory(dtoArgumentCaptor.capture());
    verifyNoMoreInteractions(commandService);

    assertThat(dtoArgumentCaptor.getValue()).extracting(SearchHistoryDto::getSort).isEqualTo(sort);
  }

  @ParameterizedTest
  @EnumSource(Status.class)
  void shouldSetStatusParameter(Status status) throws SQLException {
    doNothingWhenSearchHistory();
    whenCmdGetParameterValue(HistoryCommand.PARAMETER_FROM);
    whenCmdGetParameterValue(HistoryCommand.PARAMETER_TO);
    whenCmdGetParameterValue(HistoryCommand.PARAMETER_SORT);
    whenCmdGetParameterValue(HistoryCommand.PARAMETER_STATUS, status.name());

    command.handleCommandLine(mockCmd);

    verify(commandService).searchHistory(dtoArgumentCaptor.capture());
    verifyNoMoreInteractions(commandService);

    assertThat(dtoArgumentCaptor.getValue())
        .extracting(SearchHistoryDto::getStatus)
        .isEqualTo(status);
  }
}
