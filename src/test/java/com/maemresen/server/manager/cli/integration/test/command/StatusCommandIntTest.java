package com.maemresen.server.manager.cli.integration.test.command;

import static org.assertj.core.api.Assertions.assertThat;

import com.maemresen.server.manager.cli.integration.test.BaseApplicationIntTest;
import com.maemresen.server.manager.cli.integration.test.TestProperty;
import com.maemresen.server.manager.cli.model.entity.ServerEvent;
import com.maemresen.server.manager.cli.model.entity.Status;
import com.maemresen.server.manager.cli.utils.properties.command.UpCommandProps;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import org.junit.jupiter.api.Test;

class StatusCommandIntTest extends BaseApplicationIntTest {

  @TestProperty(key = UpCommandProps.RANDOM_WAIT_SECONDS_MIN, value = "0")
  @TestProperty(key = UpCommandProps.RANDOM_WAIT_SECONDS_MAX, value = "0")
  @TestProperty(key = UpCommandProps.FAILURE_PROBABILITY, value = "0")
  @Test
  void shouldShowUptime() throws SQLException, IOException, InterruptedException {
    application.run("up");
    application.run("status");

    List<ServerEvent> serverEvents = searchServerEvents();

    assertThat(serverEvents).hasSize(1);

    assertStatuses(Status.STARTING, Status.UP);
  }
}
