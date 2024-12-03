package com.maemresen.server.manager.cli.beans.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import ch.qos.logback.classic.spi.ILoggingEvent;
import com.maemresen.server.manager.cli.beans.service.CommandService;
import com.maemresen.server.manager.cli.utils.LogInterceptor;
import org.apache.commons.cli.CommandLine;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.event.Level;

@ExtendWith(MockitoExtension.class)
class AbstractCommandTest {

  private static final String TEST_COMMAND_NAME = "test";

  private static final LogInterceptor LOG_INTERCEPTOR =
      LogInterceptor.forClass(AbstractCommand.class, Level.TRACE);

  private static class TestCommand extends AbstractCommand {

    protected TestCommand(String name, CommandService commandService) {
      super(name, commandService);
    }

    @Override
    protected void handleCommandLine(CommandLine cmd) {
      // TEST Command
    }
  }

  @Mock private CommandService commandService;

  private TestCommand command;

  @BeforeAll
  static void setUp() {
    LOG_INTERCEPTOR.start();
  }

  @BeforeEach
  void setUpEach() {
    LOG_INTERCEPTOR.reset();
    command = spy(new TestCommand(TEST_COMMAND_NAME, commandService));
  }

  @Test
  void shouldParseCommandSuccessfully() throws InterruptedException {
    command.run();

    verify(command).handleCommandLine(any());

    assertThat(LOG_INTERCEPTOR.getLoggedEvents()).isEmpty();
  }

  @Test
  void shouldHandleCommandFailure() throws InterruptedException {
    String exceptionMessage = "test exception message";
    doThrow(new RuntimeException(exceptionMessage)).when(command).handleCommandLine(any());

    command.run();

    InOrder inOrder = Mockito.inOrder(command);
    inOrder.verify(command).handleCommandLine(any());
    inOrder.verify(command).printHelp();

    assertThat(LOG_INTERCEPTOR.getLoggedEvents())
        .hasSize(1)
        .element(0)
        .extracting(ILoggingEvent::getFormattedMessage)
        .isEqualTo(exceptionMessage);
  }
}
