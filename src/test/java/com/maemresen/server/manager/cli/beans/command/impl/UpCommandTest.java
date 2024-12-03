package com.maemresen.server.manager.cli.beans.command.impl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.maemresen.server.manager.cli.beans.service.CommandService;
import java.sql.SQLException;
import java.time.format.DateTimeParseException;
import org.apache.commons.cli.CommandLine;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UpCommandTest {

  @Mock private CommandLine mockCmd;

  @Mock private CommandService commandService;

  @InjectMocks private UpCommand command;

  private void doNothingWhenUp() throws SQLException, InterruptedException {
    doNothing().when(commandService).up();
  }

  private void doNothingWhenDown() throws SQLException, InterruptedException {
    doNothing().when(commandService).down();
  }

  private void whenCmdGetParameterValue(String value) {
    when(mockCmd.hasOption(UpCommand.PARAMETER_BEFORE)).thenReturn(true);
    when(mockCmd.getOptionValue(UpCommand.PARAMETER_BEFORE)).thenReturn(value);
  }

  @Test
  void shouldUpServer() throws InterruptedException, SQLException {
    doNothingWhenUp();

    command.handleCommandLine(mockCmd);

    verify(commandService).up();
    verifyNoMoreInteractions(commandService);
  }

  @Test
  void shouldUpAndThenDownServer() throws InterruptedException, SQLException {
    doNothingWhenUp();
    doNothingWhenDown();
    whenCmdGetParameterValue("2024-10-10 01:01");

    command.handleCommandLine(mockCmd);

    verify(commandService).up();
    verify(commandService).down();
    verifyNoMoreInteractions(commandService);
  }

  @Test
  void shouldNotDownServerWithWrongOpt() throws InterruptedException, SQLException {
    doNothingWhenUp();

    whenCmdGetParameterValue("wrong date");

    assertThatThrownBy(() -> command.handleCommandLine(mockCmd))
        .isInstanceOf(DateTimeParseException.class);

    verify(commandService).up();
    verifyNoMoreInteractions(commandService);
  }
}
