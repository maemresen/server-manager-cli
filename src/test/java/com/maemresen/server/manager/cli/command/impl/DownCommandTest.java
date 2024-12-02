package com.maemresen.server.manager.cli.command.impl;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.maemresen.server.manager.cli.service.CommandService;
import java.sql.SQLException;
import org.apache.commons.cli.CommandLine;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DownCommandTest {

  @Mock private CommandLine mockCmd;

  @Mock private CommandService commandService;

  @InjectMocks private DownCommand command;

  @Test
  void shouldRunCommand() throws InterruptedException, SQLException {
    command.handleCommandLine(mockCmd);

    verify(commandService).down();
    verifyNoMoreInteractions(commandService);
  }
}
