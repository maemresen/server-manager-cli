package com.maemresen.server.manager.cli.integration.test.command;

import static org.assertj.core.api.Assertions.assertThat;

import ch.qos.logback.classic.spi.ILoggingEvent;
import com.maemresen.server.manager.cli.integration.test.BaseApplicationIntTest;
import com.maemresen.server.manager.cli.model.entity.Status;
import java.io.IOException;
import java.sql.SQLException;
import org.junit.jupiter.api.Test;

class HistoryCommandIntTest extends BaseApplicationIntTest {

  @Test
  void shouldListAllHistoryFromNewestToOldest()
      throws SQLException, IOException, InterruptedException {
    createServerEvent("2024-12-04", Status.DOWN);
    createServerEvent("2024-12-03", Status.STOPPING);
    createServerEvent("2024-12-02", Status.UP);
    createServerEvent("2024-12-01", Status.STARTING);

    application.run("history");

    assertHistoryLog(0, "2024-12-04 00:00:00", Status.DOWN);
    assertHistoryLog(1, "2024-12-03 00:00:00", Status.STOPPING);
    assertHistoryLog(2, "2024-12-02 00:00:00", Status.UP);
    assertHistoryLog(3, "2024-12-01 00:00:00", Status.STARTING);
  }

  @Test
  void shouldFilterByStatus() throws SQLException, IOException, InterruptedException {
    createServerEvent("2024-12-04", Status.DOWN);
    createServerEvent("2024-12-03", Status.UP);
    createServerEvent("2024-12-02", Status.UP);
    createServerEvent("2024-12-01", Status.STARTING);

    application.run("history", "--status", "UP");

    assertHistoryLog(0, "2024-12-03 00:00:00", Status.UP);
    assertHistoryLog(1, "2024-12-02 00:00:00", Status.UP);
  }

  @Test
  void shouldSortByNewestToOldest() throws SQLException, IOException, InterruptedException {
    createServerEvent("2024-12-04", Status.DOWN);
    createServerEvent("2024-12-03", Status.STOPPING);
    createServerEvent("2024-12-02", Status.UP);
    createServerEvent("2024-12-01", Status.STARTING);

    application.run("history", "--sort", "DESC");

    assertHistoryLog(0, "2024-12-04 00:00:00", Status.DOWN);
    assertHistoryLog(1, "2024-12-03 00:00:00", Status.STOPPING);
    assertHistoryLog(2, "2024-12-02 00:00:00", Status.UP);
    assertHistoryLog(3, "2024-12-01 00:00:00", Status.STARTING);
  }

  @Test
  void shouldSortByOldestToNewest() throws SQLException, IOException, InterruptedException {
    createServerEvent("2024-12-04", Status.DOWN);
    createServerEvent("2024-12-03", Status.STOPPING);
    createServerEvent("2024-12-02", Status.UP);
    createServerEvent("2024-12-01", Status.STARTING);

    application.run("history", "--sort", "ASC");

    assertHistoryLog(0, "2024-12-01 00:00:00", Status.STARTING);
    assertHistoryLog(1, "2024-12-02 00:00:00", Status.UP);
    assertHistoryLog(2, "2024-12-03 00:00:00", Status.STOPPING);
    assertHistoryLog(3, "2024-12-04 00:00:00", Status.DOWN);
  }

  @Test
  void shouldFilterOutBeforeFrom() throws SQLException, IOException, InterruptedException {
    createServerEvent("2024-12-04", Status.DOWN);
    createServerEvent("2024-12-03", Status.STOPPING);
    createServerEvent("2024-12-02", Status.UP);
    createServerEvent("2024-12-01", Status.STARTING);

    application.run("history", "--from", "2024-12-02");

    assertHistoryLog(0, "2024-12-04 00:00:00", Status.DOWN);
    assertHistoryLog(1, "2024-12-03 00:00:00", Status.STOPPING);
    assertHistoryLog(2, "2024-12-02 00:00:00", Status.UP);
  }

  @Test
  void shouldFilterOutAfterTo() throws SQLException, IOException, InterruptedException {
    createServerEvent("2024-12-04", Status.DOWN);
    createServerEvent("2024-12-03", Status.STOPPING);
    createServerEvent("2024-12-02", Status.UP);
    createServerEvent("2024-12-01", Status.STARTING);

    application.run("history", "--to", "2024-12-03");

    assertHistoryLog(0, "2024-12-03 00:00:00", Status.STOPPING);
    assertHistoryLog(1, "2024-12-02 00:00:00", Status.UP);
    assertHistoryLog(2, "2024-12-01 00:00:00", Status.STARTING);
  }

  @Test
  void shouldFilterOutBeforeFromAndAfterTo()
      throws SQLException, IOException, InterruptedException {
    createServerEvent("2024-12-05", Status.FAILED);
    createServerEvent("2024-12-04", Status.DOWN);
    createServerEvent("2024-12-03", Status.STOPPING);
    createServerEvent("2024-12-02", Status.UP);
    createServerEvent("2024-12-01", Status.STARTING);

    application.run("history", "--from", "2024-12-02", "--to", "2024-12-04");

    assertHistoryLog(0, "2024-12-04 00:00:00", Status.DOWN);
    assertHistoryLog(1, "2024-12-03 00:00:00", Status.STOPPING);
    assertHistoryLog(2, "2024-12-02 00:00:00", Status.UP);
  }

  private void assertHistoryLog(int index, String creationTime, Status status) {
    assertThat(getLoggedEvents())
        .element(index)
        .extracting(ILoggingEvent::getFormattedMessage)
        .isEqualTo(String.format("%s %s", creationTime, status));
  }
}
