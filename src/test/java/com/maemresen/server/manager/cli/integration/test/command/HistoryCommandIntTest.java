package com.maemresen.server.manager.cli.integration.test.command;

import static org.assertj.core.api.Assertions.assertThat;

import ch.qos.logback.classic.spi.ILoggingEvent;
import com.maemresen.server.manager.cli.integration.test.BaseApplicationIntTest;
import com.maemresen.server.manager.cli.model.dto.Sort;
import com.maemresen.server.manager.cli.model.entity.ServerEvent;
import com.maemresen.server.manager.cli.utils.DateTimeUtils;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

class HistoryCommandIntTest extends BaseApplicationIntTest {

  @Test
  void shouldListAllHistoryFromNewestToOldest() throws SQLException, IOException, InterruptedException {
    application.run("up");
    application.run("down");

    resetLogger();
    application.run("history");

    List<ServerEvent> serverEvents = searchServerEvents(Sort.DESC);

    IntStream.range(0, serverEvents.size())
        .forEach(
            index -> {
              ServerEvent serverEvent = serverEvents.get(index);
              LocalDateTime creationTime = serverEvent.getCreationTime();
              String creationTimeString = DateTimeUtils.formatDateTimeWithSeconds(creationTime);
              assertThat(LOG_INTERCEPTOR.getLoggedEvents())
                  .element(index)
                  .extracting(ILoggingEvent::getFormattedMessage)
                  .isEqualTo("%s %s".formatted(creationTimeString, serverEvent.getStatus()));
            });
  }

  @Test
  void shouldFilterByStatus() throws SQLException, IOException, InterruptedException {
    application.run("up");
  }
}
