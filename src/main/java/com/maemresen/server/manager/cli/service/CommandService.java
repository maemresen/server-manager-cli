package com.maemresen.server.manager.cli.service;

import com.maemresen.server.manager.cli.exception.RandomServerException;
import com.maemresen.server.manager.cli.model.dto.SearchHistoryDto;
import com.maemresen.server.manager.cli.model.entity.ServerEvent;
import com.maemresen.server.manager.cli.model.entity.Status;
import com.maemresen.server.manager.cli.repository.ServerEventRepository;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommandService {
  private final ServerEventRepository serverEventRepository = new ServerEventRepository();
  private final RandomActionService randomActionService = new RandomActionService();

  public void status() throws SQLException {
    Optional<ServerEvent> latest = serverEventRepository.findLatest();
    if (latest.isEmpty()) {
      log.info("No history found for the server.");
      return;
    }

    ServerEvent serverEvent = latest.get();
    Status status = serverEvent.getStatus();
    if (status == Status.UP) {
      Duration uptime = Duration.between(LocalDateTime.now(), serverEvent.getCreationTime()).abs();
      log.info("{} {}", uptime, serverEvent.getStatus());
    } else {
      log.info("{}", serverEvent.getStatus());
    }
  }

  public void up() throws InterruptedException, SQLException {
    if (isLatestStatus(Status.UP)) {
      log.info("Already up.");
      return;
    }

    serverEventRepository.createNewEvent(Status.STARTING);
    log.info("Starting...");
    randomActionService.waitRandomSeconds(3, 10);
    try {
      randomActionService.throwRandomException(
          20, () -> new RandomServerException("Failed to starting server."));
      serverEventRepository.createNewEvent(Status.UP);
      log.info("Started.");
    } catch (RandomServerException exception) {
      createFailure();
    }
  }

  public void down() throws InterruptedException, SQLException {
    if (isLatestStatus(Status.DOWN)) {
      log.info("Already down.");
      return;
    }

    serverEventRepository.createNewEvent(Status.STOPPING);
    log.info("Stopping...");
    randomActionService.waitRandomSeconds(3, 10);
    try {
      randomActionService.throwRandomException(
          20, () -> new RandomServerException("Failed to starting server."));
      serverEventRepository.createNewEvent(Status.DOWN);
      log.info("Stopped.");
    } catch (RandomServerException exception) {
      createFailure();
    }
  }

  public void history(SearchHistoryDto searchHistoryDto) throws SQLException {
    List<ServerEvent> serverEvents = serverEventRepository.searchHistory(searchHistoryDto);
    if (serverEvents.isEmpty()) {
      log.info("No events found.");
      return;
    }

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    serverEvents.forEach(
        serverEvent -> {
          String formattedTime = serverEvent.getCreationTime().format(formatter);
          String statusMessage = formatStatusLog(serverEvent.getStatus(), formattedTime);
          log.info(statusMessage);
        });
  }

  private String formatStatusLog(Status status, String formattedTime) {
    return switch (status) {
      case UP -> String.format("Server is UP since %s", formattedTime);
      case DOWN -> String.format("Server went DOWN at %s", formattedTime);
      case STARTING -> String.format("Server is STARTING as of %s", formattedTime);
      case STOPPING -> String.format("Server is STOPPING as of %s", formattedTime);
      case FAILED -> String.format("Server FAILED at %s", formattedTime);
    };
  }

  private boolean isLatestStatus(Status status) throws SQLException {
    return serverEventRepository
        .findLatest()
        .map(ServerEvent::getStatus)
        .filter(status::equals)
        .isPresent();
  }

  private void createFailure() throws SQLException {
    serverEventRepository.createNewEvent(Status.FAILED);
    log.info("Failed.");
  }
}