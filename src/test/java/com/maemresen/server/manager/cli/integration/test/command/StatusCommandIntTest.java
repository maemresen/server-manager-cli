package com.maemresen.server.manager.cli.integration.test.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.STRING;

import ch.qos.logback.classic.spi.ILoggingEvent;
import com.maemresen.server.manager.cli.integration.test.BaseApplicationIntTest;
import com.maemresen.server.manager.cli.integration.test.TestProperty;
import com.maemresen.server.manager.cli.model.entity.Status;
import com.maemresen.server.manager.cli.utils.properties.command.DownCommandProps;
import com.maemresen.server.manager.cli.utils.properties.command.UpCommandProps;
import java.io.IOException;
import java.sql.SQLException;
import org.junit.jupiter.api.Test;

class StatusCommandIntTest extends BaseApplicationIntTest {

  private static final String DURATION_STRING_PATTERN = "^\\d{2}:\\d{2}:\\d{2} UP$";

  @Test
  void shouldLatestStatus() throws SQLException, IOException, InterruptedException {
    application.run("up");

    resetLogger();
    application.run("status");
    assertThat(LOG_INTERCEPTOR.getLoggedEvents())
        .element(0)
        .extracting(ILoggingEvent::getFormattedMessage)
        .asInstanceOf(STRING)
        .matches(DURATION_STRING_PATTERN);

    application.run("down");
    resetLogger();

    application.run("status");
    assertThat(LOG_INTERCEPTOR.getLoggedEvents())
        .element(0)
        .extracting(ILoggingEvent::getFormattedMessage)
        .asInstanceOf(STRING)
        .matches("DOWN");

    assertStatuses(Status.STARTING, Status.UP, Status.STOPPING, Status.DOWN);
  }

  @TestProperty(key = UpCommandProps.FAILURE_PROBABILITY, value = "100")
  @TestProperty(key = DownCommandProps.FAILURE_PROBABILITY, value = "100")
  @Test
  void shouldShowLatestFailure() throws SQLException, IOException, InterruptedException {
    application.run("up");

    resetLogger();
    application.run("status");
    assertThat(LOG_INTERCEPTOR.getLoggedEvents())
        .element(0)
        .extracting(ILoggingEvent::getFormattedMessage)
        .asInstanceOf(STRING)
        .matches("FAILED");

    application.run("down");

    resetLogger();
    application.run("status");
    assertThat(LOG_INTERCEPTOR.getLoggedEvents())
        .element(0)
        .extracting(ILoggingEvent::getFormattedMessage)
        .asInstanceOf(STRING)
        .matches("FAILED");

    assertStatuses(Status.STARTING, Status.FAILED, Status.STOPPING, Status.FAILED);
  }
}
