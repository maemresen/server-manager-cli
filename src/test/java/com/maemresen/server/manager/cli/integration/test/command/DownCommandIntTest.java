package com.maemresen.server.manager.cli.integration.test.command;

import com.maemresen.server.manager.cli.integration.test.BaseApplicationIntTest;
import com.maemresen.server.manager.cli.integration.test.TestProperty;
import com.maemresen.server.manager.cli.model.entity.Status;
import com.maemresen.server.manager.cli.utils.properties.command.DownCommandProps;
import java.sql.SQLException;
import org.junit.jupiter.api.Test;

class DownCommandIntTest extends BaseApplicationIntTest {

  @Test
  void shouldCreateStoppingAndUpEvent() throws SQLException, InterruptedException {
    application.run("down");

    assertStatuses(Status.STOPPING, Status.DOWN);
    assertLogMessages("Stopping...", "Stopped.");
  }

  @TestProperty(key = DownCommandProps.FAILURE_PROBABILITY, value = "100")
  @Test
  void shouldCreateStoppingAndFailureEvent() throws SQLException, InterruptedException {
    application.run("down");

    assertStatuses(Status.STOPPING, Status.FAILED);
    assertLogMessages("Stopping...", "Failed.");
  }

  @Test
  void shouldNotCreateEventForAlreadyStoppedServer() throws SQLException, InterruptedException {

    application.run("down");
    application.run("down");

    assertStatuses(Status.STOPPING, Status.DOWN);
    assertLogMessages("Stopping...", "Stopped.", "Already down.");
  }
}
