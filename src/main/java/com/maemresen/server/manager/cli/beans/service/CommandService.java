package com.maemresen.server.manager.cli.beans.service;

import com.google.inject.Inject;
import com.maemresen.server.manager.cli.beans.AppProps;
import com.maemresen.server.manager.cli.beans.repository.ServerEventRepository;
import com.maemresen.server.manager.cli.exception.RandomServerException;
import com.maemresen.server.manager.cli.model.dto.SearchHistoryDto;
import com.maemresen.server.manager.cli.model.entity.ServerEvent;
import com.maemresen.server.manager.cli.model.entity.Status;
import com.maemresen.server.manager.cli.utils.DateTimeUtils;
import com.maemresen.server.manager.cli.utils.RandomActionHelper;
import com.maemresen.server.manager.cli.utils.properties.command.DownCommandProps;
import com.maemresen.server.manager.cli.utils.properties.command.UpCommandProps;
import java.sql.SQLException;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommandService {
  private final ServerEventRepository serverEventRepository;
  private final AppProps appProps;

  @Inject
  public CommandService(ServerEventRepository serverEventRepository, AppProps appProps) {
    this.serverEventRepository = serverEventRepository;
    this.appProps = appProps;
  }

  public void status() throws SQLException {
    Optional<ServerEvent> latest = serverEventRepository.findLatest();
    if (latest.isEmpty()) {
      log.info("No searchHistory found for the server.");
      return;
    }

    ServerEvent serverEvent = latest.get();
    Status status = serverEvent.getStatus();
    if (status == Status.UP) {
      Duration uptime = Duration.between(serverEvent.getCreationTime(), DateTimeUtils.now());
      log.info("{} {}", DateTimeUtils.formatDuration(uptime), serverEvent.getStatus());
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
    int min = appProps.getIntProp(UpCommandProps.RANDOM_WAIT_SECONDS_MIN).orElse(3);
    int max = appProps.getIntProp(UpCommandProps.RANDOM_WAIT_SECONDS_MAX).orElse(10);
    RandomActionHelper.waitRandomSeconds(min, max);
    try {
      int failureProbability = appProps.getIntProp(UpCommandProps.FAILURE_PROBABILITY).orElse(20);
      RandomActionHelper.throwRandomException(failureProbability, this::startServerException);
      serverEventRepository.createNewEvent(Status.UP);
      log.info("Started.");
    } catch (RandomServerException exception) {
      createFailure();
    }
  }

  private RandomServerException startServerException() {
    return new RandomServerException("Failed to starting server.");
  }

  public void down() throws InterruptedException, SQLException {
    if (isLatestStatus(Status.DOWN)) {
      log.info("Already down.");
      return;
    }

    serverEventRepository.createNewEvent(Status.STOPPING);
    log.info("Stopping...");
    int min = appProps.getIntProp(DownCommandProps.RANDOM_WAIT_SECONDS_MIN).orElse(3);
    int max = appProps.getIntProp(DownCommandProps.RANDOM_WAIT_SECONDS_MAX).orElse(10);
    RandomActionHelper.waitRandomSeconds(min, max);
    try {
      int failureProbability = appProps.getIntProp(DownCommandProps.FAILURE_PROBABILITY).orElse(20);
      RandomActionHelper.throwRandomException(failureProbability, this::stopServerException);
      serverEventRepository.createNewEvent(Status.DOWN);
      log.info("Stopped.");
    } catch (RandomServerException exception) {
      createFailure();
    }
  }

  private RandomServerException stopServerException() {
    return new RandomServerException("Failed to stopping server.");
  }

  public void searchHistory(SearchHistoryDto searchHistoryDto) throws SQLException {
    List<ServerEvent> serverEvents = serverEventRepository.searchHistory(searchHistoryDto);
    if (serverEvents.isEmpty()) {
      log.info("No events found.");
      return;
    }

    serverEvents.forEach(
        serverEvent -> {
          String formattedTime =
              DateTimeUtils.formatDateTimeWithSeconds(serverEvent.getCreationTime());
          log.info("{} {}", formattedTime, serverEvent.getStatus());
        });
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
