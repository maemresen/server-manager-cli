package com.maemresen.server.manager.cli.integration.test.command;

import com.maemresen.server.manager.cli.integration.test.BaseApplicationIntTest;
import com.maemresen.server.manager.cli.integration.test.TestProperty;
import com.maemresen.server.manager.cli.model.entity.Status;
import com.maemresen.server.manager.cli.utils.DateTimeUtils;
import com.maemresen.server.manager.cli.utils.properties.command.DownCommandProps;
import com.maemresen.server.manager.cli.utils.properties.command.UpCommandProps;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class UpCommandIntTest extends BaseApplicationIntTest {

  @TestProperty(key = UpCommandProps.RANDOM_WAIT_SECONDS_MIN, value = "0")
  @TestProperty(key = UpCommandProps.RANDOM_WAIT_SECONDS_MAX, value = "0")
  @TestProperty(key = UpCommandProps.FAILURE_PROBABILITY, value = "0")
  @Test
  void shouldCreateStartingAndUpEvent() throws SQLException, IOException, InterruptedException {
    application.run("up");

    assertStatuses(Status.STARTING, Status.UP);
    assertLogMessages("Starting...", "Started.");
  }

  @TestProperty(key = UpCommandProps.RANDOM_WAIT_SECONDS_MIN, value = "0")
  @TestProperty(key = UpCommandProps.RANDOM_WAIT_SECONDS_MAX, value = "0")
  @TestProperty(key = UpCommandProps.FAILURE_PROBABILITY, value = "100")
  @Test
  void shouldCreateStartingAndFailureEvent()
      throws SQLException, IOException, InterruptedException {
    application.run("up");

    assertStatuses(Status.STARTING, Status.FAILED);
    assertLogMessages("Starting...", "Failed.");
  }

  @TestProperty(key = UpCommandProps.RANDOM_WAIT_SECONDS_MIN, value = "0")
  @TestProperty(key = UpCommandProps.RANDOM_WAIT_SECONDS_MAX, value = "0")
  @TestProperty(key = UpCommandProps.FAILURE_PROBABILITY, value = "0")
  @Test
  void shouldNotCreateEventForAlreadyStartedServer()
      throws SQLException, IOException, InterruptedException {

    application.run("up");
    application.run("up");

    assertStatuses(Status.STARTING, Status.UP);
    assertLogMessages("Starting...", "Started.", "Already up.");
  }

  @TestProperty(key = UpCommandProps.RANDOM_WAIT_SECONDS_MIN, value = "0")
  @TestProperty(key = UpCommandProps.RANDOM_WAIT_SECONDS_MAX, value = "0")
  @TestProperty(key = UpCommandProps.FAILURE_PROBABILITY, value = "0")
  @TestProperty(key = DownCommandProps.RANDOM_WAIT_SECONDS_MIN, value = "0")
  @TestProperty(key = DownCommandProps.RANDOM_WAIT_SECONDS_MAX, value = "0")
  @TestProperty(key = DownCommandProps.FAILURE_PROBABILITY, value = "0")
  @Test
  void shouldDownServerIfBeforeParameterPassed()
      throws SQLException, IOException, InterruptedException {

    LocalDateTime now = DateTimeUtils.now();
    String beforeParameter = DateTimeUtils.DATE_TIME_PARAMETER_PATTERN.format(now.plusSeconds(1));

    application.run("up", "--before", beforeParameter);

    assertStatuses(Status.STARTING, Status.UP, Status.STOPPING, Status.DOWN);
    assertLogMessages("Starting...", "Started.", "Stopping...", "Stopped.");
  }
}
