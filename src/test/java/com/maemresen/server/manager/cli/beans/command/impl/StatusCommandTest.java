package com.maemresen.server.manager.cli.beans.command.impl;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.maemresen.server.manager.cli.beans.service.CommandService;
import java.sql.SQLException;
import org.apache.commons.cli.CommandLine;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StatusCommandTest {

  @Mock private CommandLine mockCmd;

  @Mock private CommandService commandService;

  @InjectMocks private StatusCommand command;

  @Test
  void shouldRunCommand() throws SQLException {
    command.handleCommandLine(mockCmd);

    verify(commandService).status();
    verifyNoMoreInteractions(commandService);
  }
}
