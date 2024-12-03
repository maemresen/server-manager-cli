package com.maemresen.server.manager.cli.integration.test;

import static org.assertj.core.api.Assertions.assertThat;

import ch.qos.logback.classic.spi.ILoggingEvent;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.maemresen.server.manager.cli.beans.Application;
import com.maemresen.server.manager.cli.beans.repository.ServerEventRepository;
import com.maemresen.server.manager.cli.beans.service.CommandService;
import com.maemresen.server.manager.cli.model.dto.SearchHistoryDto;
import com.maemresen.server.manager.cli.model.dto.Sort;
import com.maemresen.server.manager.cli.model.entity.ServerEvent;
import com.maemresen.server.manager.cli.model.entity.Status;
import com.maemresen.server.manager.cli.utils.DateTimeUtils;
import com.maemresen.server.manager.cli.utils.LogInterceptor;
import com.maemresen.server.manager.cli.utils.properties.DbProps;
import com.maemresen.server.manager.cli.utils.properties.Property;
import com.maemresen.server.manager.cli.utils.properties.command.DownCommandProps;
import com.maemresen.server.manager.cli.utils.properties.command.UpCommandProps;
import java.io.IOException;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.slf4j.event.Level;

class UpCommandIntTest extends BaseApplicationIntTest{

  @Test
  void shouldCreateStartingAndUpEvent() throws SQLException, IOException, InterruptedException {
    initializeContext(
        Map.of(
            UpCommandProps.RANDOM_WAIT_SECONDS_MIN, "0",
            UpCommandProps.RANDOM_WAIT_SECONDS_MAX, "0",
            UpCommandProps.FAILURE_PROBABILITY, "0"));

    application.run("up");

    assertStatuses(Status.STARTING, Status.UP);
    assertLogMessages("Starting...", "Started.");
  }

  @Test
  void shouldCreateStartingAndFailureEvent()
      throws SQLException, IOException, InterruptedException {
    initializeContext(
        Map.of(
            UpCommandProps.RANDOM_WAIT_SECONDS_MIN, "0",
            UpCommandProps.RANDOM_WAIT_SECONDS_MAX, "0",
            UpCommandProps.FAILURE_PROBABILITY, "100"));

    application.run("up");

    assertStatuses(Status.STARTING, Status.FAILED);
    assertLogMessages("Starting...", "Failed.");
  }

  @Test
  void shouldNotCreateEventForAlreadyStartedServer()
      throws SQLException, IOException, InterruptedException {
    initializeContext(
        Map.of(
            UpCommandProps.RANDOM_WAIT_SECONDS_MIN, "0",
            UpCommandProps.RANDOM_WAIT_SECONDS_MAX, "0",
            UpCommandProps.FAILURE_PROBABILITY, "0"));

    application.run("up");
    application.run("up");

    assertStatuses(Status.STARTING, Status.UP);
    assertLogMessages("Starting...", "Started.", "Already up.");
  }

  @Test
  void shouldDownServerIfBeforeParameterPassed()
      throws SQLException, IOException, InterruptedException {
    initializeContext(
        Map.of(
            UpCommandProps.RANDOM_WAIT_SECONDS_MIN, "0",
            UpCommandProps.RANDOM_WAIT_SECONDS_MAX, "0",
            UpCommandProps.FAILURE_PROBABILITY, "0",
            DownCommandProps.RANDOM_WAIT_SECONDS_MIN, "0",
            DownCommandProps.RANDOM_WAIT_SECONDS_MAX, "0",
            DownCommandProps.FAILURE_PROBABILITY, "0"));

    LocalDateTime now = DateTimeUtils.now();
    String beforeParameter = DateTimeUtils.DATE_TIME_PARAMETER_PATTERN.format(now.plusSeconds(1));

    application.run("up", "--before", beforeParameter);

    assertStatuses(Status.STARTING, Status.UP, Status.STOPPING, Status.DOWN);
    assertLogMessages("Starting...", "Started.", "Stopping...", "Stopped.");
  }
}
