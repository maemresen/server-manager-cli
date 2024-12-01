package com.maemresen.server.manager.cli.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.ARRAY;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import ch.qos.logback.classic.spi.ILoggingEvent;
import com.maemresen.server.manager.cli.exception.RandomServerException;
import com.maemresen.server.manager.cli.model.dto.SearchHistoryDto;
import com.maemresen.server.manager.cli.model.entity.ServerEvent;
import com.maemresen.server.manager.cli.model.entity.Status;
import com.maemresen.server.manager.cli.repository.ServerEventRepository;
import com.maemresen.server.manager.cli.utils.DateTimeUtils;
import com.maemresen.server.manager.cli.utils.LogInterceptor;
import com.maemresen.server.manager.cli.utils.RandomActionHelper;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Answers;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.event.Level;

@ExtendWith(MockitoExtension.class)
class CommandServiceTest {

  private static final LocalDateTime NOW = LocalDateTime.now();
  private static final LogInterceptor LOG_INTERCEPTOR =
      LogInterceptor.forClass(CommandService.class, Level.TRACE);

  @Mock private ServerEventRepository serverEventRepository;

  @InjectMocks private CommandService commandService;

  private ServerEvent latestServerEvent;

  @BeforeAll
  static void setUp() {
    LOG_INTERCEPTOR.start();
  }

  @BeforeEach
  void setUpEach() {
    LOG_INTERCEPTOR.reset();
    latestServerEvent = null;
  }

  private void whenLatestEvent() throws SQLException {
    when(serverEventRepository.findLatest()).thenReturn(Optional.ofNullable(latestServerEvent));
  }

  private void doNothingWhenThrowRandomException(MockedStatic<RandomActionHelper> mockedStatic) {
    mockedStatic
        .when(() -> RandomActionHelper.throwRandomException(anyInt(), any()))
        .then(invocationOnMock -> null);
  }

  private void doRandomExceptionWhenThrowRandomException(
      MockedStatic<RandomActionHelper> mockedStatic) {
    mockedStatic
        .when(() -> RandomActionHelper.throwRandomException(anyInt(), any()))
        .thenThrow(RandomServerException.class);
  }

  private ServerEvent createServerEvent(Status status) {
    return createServerEvent(status, NOW);
  }

  private ServerEvent createServerEvent(Status status, LocalDateTime creationTime) {
    return ServerEvent.builder().status(status).creationTime(creationTime).build();
  }

  @Nested
  class StatusCommandTest {
    // mockStatic(RandomActionHelper.class)

    @Test
    void shouldOutputNoHistory() throws SQLException {
      whenLatestEvent();

      commandService.status();

      verify(serverEventRepository).findLatest();

      assertThat(LOG_INTERCEPTOR.getLoggedEvents())
          .hasSize(1)
          .element(0)
          .extracting(ILoggingEvent::getFormattedMessage)
          .isEqualTo("No history found for the server.");
    }

    @ParameterizedTest
    @EnumSource(value = Status.class, names = "UP", mode = EnumSource.Mode.EXCLUDE)
    void shouldOutputLastEvent(Status latestStatus) throws SQLException {
      latestServerEvent = createServerEvent(latestStatus);
      whenLatestEvent();

      commandService.status();

      verify(serverEventRepository).findLatest();

      assertThat(LOG_INTERCEPTOR.getLoggedEvents())
          .hasSize(1)
          .element(0)
          .extracting(ILoggingEvent::getArgumentArray)
          .asInstanceOf(ARRAY)
          .containsExactly(latestStatus);
    }

    @Test
    void shouldOutputLastEventAndUptime() throws SQLException {

      final String uptimeString = "01:01:01";
      try (final MockedStatic<DateTimeUtils> mockedDateTimeUtils = mockStatic(DateTimeUtils.class)) {
        mockedDateTimeUtils
            .when(() -> DateTimeUtils.formatDuration(any()))
            .thenReturn(uptimeString);
        latestServerEvent = new ServerEvent();
        latestServerEvent.setStatus(Status.UP);
        latestServerEvent.setCreationTime(LocalDateTime.now());

        whenLatestEvent();

        commandService.status();

        verify(serverEventRepository).findLatest();

        assertThat(LOG_INTERCEPTOR.getLoggedEvents())
            .hasSize(1)
            .element(0)
            .extracting(ILoggingEvent::getFormattedMessage)
            .isEqualTo(String.format("%s %s", uptimeString, Status.UP.name()));
      }
    }
  }

  @Nested
  class UpCommandTest {
    @Test
    void shouldDoNothingWhenTheServerIsAlreadyUp() throws InterruptedException, SQLException {
      latestServerEvent = new ServerEvent();
      latestServerEvent.setStatus(Status.UP);

      whenLatestEvent();

      try (final MockedStatic<RandomActionHelper> mockedRandomActionHelper = mockStatic(RandomActionHelper.class)) {
        commandService.up();

        verify(serverEventRepository).findLatest();
        verifyNoMoreInteractions(serverEventRepository);

        mockedRandomActionHelper.verify(
            () -> RandomActionHelper.waitRandomSeconds(anyInt(), anyInt()), never());
        mockedRandomActionHelper.verify(
            () -> RandomActionHelper.throwRandomException(anyInt(), any()), never());
      }

      assertThat(LOG_INTERCEPTOR.getLoggedEvents())
          .hasSize(1)
          .element(0)
          .extracting(ILoggingEvent::getFormattedMessage)
          .isEqualTo("Already up.");
    }

    @Test
    void shouldSuccessfullyCreateEventsToStartTheServer()
        throws InterruptedException, SQLException {
      whenLatestEvent();
      try (final MockedStatic<RandomActionHelper> mockedRandomActionHelper =
          mockStatic(RandomActionHelper.class, Answers.CALLS_REAL_METHODS)) {
        doNothingWhenThrowRandomException(mockedRandomActionHelper);

        commandService.up();

        InOrder inOrder = Mockito.inOrder(serverEventRepository, RandomActionHelper.class);
        inOrder.verify(serverEventRepository).findLatest();
        inOrder.verify(serverEventRepository).createNewEvent(Status.STARTING);
        inOrder.verify(
            mockedRandomActionHelper,
            () -> RandomActionHelper.waitRandomSeconds(anyInt(), anyInt()));
        inOrder.verify(
            mockedRandomActionHelper,
            () -> RandomActionHelper.throwRandomException(anyInt(), any()));
        inOrder.verify(serverEventRepository).createNewEvent(Status.UP);
        verifyNoMoreInteractions(serverEventRepository);

        assertThat(LOG_INTERCEPTOR.getLoggedEvents())
            .hasSize(2)
            .map(ILoggingEvent::getFormattedMessage)
            .containsExactly("Starting...", "Started.");
      }
    }

    @Test
    void shouldFailToStartTheServer() throws InterruptedException, SQLException {
      whenLatestEvent();
      try (final MockedStatic<RandomActionHelper> mockedRandomActionHelper =
          mockStatic(RandomActionHelper.class, Answers.CALLS_REAL_METHODS)) {
        doRandomExceptionWhenThrowRandomException(mockedRandomActionHelper);

        commandService.up();

        InOrder inOrder = Mockito.inOrder(serverEventRepository, RandomActionHelper.class);
        inOrder.verify(serverEventRepository).findLatest();
        inOrder.verify(serverEventRepository).createNewEvent(Status.STARTING);
        inOrder.verify(
            mockedRandomActionHelper,
            () -> RandomActionHelper.waitRandomSeconds(anyInt(), anyInt()));
        inOrder.verify(
            mockedRandomActionHelper,
            () -> RandomActionHelper.throwRandomException(anyInt(), any()));
        inOrder.verify(serverEventRepository).createNewEvent(Status.FAILED);
        verifyNoMoreInteractions(serverEventRepository);

        assertThat(LOG_INTERCEPTOR.getLoggedEvents())
            .hasSize(2)
            .map(ILoggingEvent::getFormattedMessage)
            .containsExactly("Starting...", "Failed.");
      }
    }
  }

  @Nested
  class DownCommandTest {
    @Test
    void shouldDoNothingWhenTheServerIsAlreadyDown() throws SQLException, InterruptedException {
      latestServerEvent = new ServerEvent();
      latestServerEvent.setStatus(Status.DOWN);

      whenLatestEvent();

      try (final MockedStatic<RandomActionHelper> mockedRandomActionHelper = mockStatic(RandomActionHelper.class)) {
        commandService.down();

        verify(serverEventRepository).findLatest();
        verifyNoMoreInteractions(serverEventRepository);

        mockedRandomActionHelper.verify(
            () -> RandomActionHelper.waitRandomSeconds(anyInt(), anyInt()), never());
        mockedRandomActionHelper.verify(
            () -> RandomActionHelper.throwRandomException(anyInt(), any()), never());
      }

      assertThat(LOG_INTERCEPTOR.getLoggedEvents())
          .hasSize(1)
          .element(0)
          .extracting(ILoggingEvent::getFormattedMessage)
          .isEqualTo("Already down.");
    }

    @Test
    void shouldSuccessfullyCreateEventsToStopTheServer() throws SQLException, InterruptedException {
      whenLatestEvent();
      try (final MockedStatic<RandomActionHelper> mockedRandomActionHelper =
          mockStatic(RandomActionHelper.class, Answers.CALLS_REAL_METHODS)) {
        doNothingWhenThrowRandomException(mockedRandomActionHelper);

        commandService.down();

        InOrder inOrder = Mockito.inOrder(serverEventRepository, RandomActionHelper.class);
        inOrder.verify(serverEventRepository).findLatest();
        inOrder.verify(serverEventRepository).createNewEvent(Status.STOPPING);
        inOrder.verify(
            mockedRandomActionHelper,
            () -> RandomActionHelper.waitRandomSeconds(anyInt(), anyInt()));
        inOrder.verify(
            mockedRandomActionHelper,
            () -> RandomActionHelper.throwRandomException(anyInt(), any()));
        inOrder.verify(serverEventRepository).createNewEvent(Status.DOWN);
        verifyNoMoreInteractions(serverEventRepository);

        assertThat(LOG_INTERCEPTOR.getLoggedEvents())
            .hasSize(2)
            .map(ILoggingEvent::getFormattedMessage)
            .containsExactly("Stopping...", "Stopped.");
      }
    }

    @Test
    void shouldFailToStopTheServer() throws SQLException, InterruptedException {
      whenLatestEvent();
      try (final MockedStatic<RandomActionHelper> mockedRandomActionHelper =
          mockStatic(RandomActionHelper.class, Answers.CALLS_REAL_METHODS)) {
        doRandomExceptionWhenThrowRandomException(mockedRandomActionHelper);

        commandService.down();

        InOrder inOrder = Mockito.inOrder(serverEventRepository, RandomActionHelper.class);
        inOrder.verify(serverEventRepository).findLatest();
        inOrder.verify(serverEventRepository).createNewEvent(Status.STOPPING);
        inOrder.verify(
            mockedRandomActionHelper,
            () -> RandomActionHelper.waitRandomSeconds(anyInt(), anyInt()));
        inOrder.verify(
            mockedRandomActionHelper,
            () -> RandomActionHelper.throwRandomException(anyInt(), any()));
        inOrder.verify(serverEventRepository).createNewEvent(Status.FAILED);
        verifyNoMoreInteractions(serverEventRepository);

        assertThat(LOG_INTERCEPTOR.getLoggedEvents())
            .hasSize(2)
            .map(ILoggingEvent::getFormattedMessage)
            .containsExactly("Stopping...", "Failed.");
      }
    }
  }

  @Nested
  class HistoryCommandTest {
    private List<ServerEvent> serverEvents;
    private SearchHistoryDto searchHistoryDto;

    @BeforeEach
    void setUpEach() {
      searchHistoryDto = null;
      serverEvents = new ArrayList<>();
    }

    private void whenSearchHistory() throws SQLException {
      when(serverEventRepository.searchHistory(searchHistoryDto)).thenReturn(serverEvents);
    }

    private void verifySearchHistory() throws SQLException {
      verify(serverEventRepository).searchHistory(searchHistoryDto);
    }

    private void addEventToHistory(ServerEvent serverEvent) {
      serverEvents.add(serverEvent);
    }

    @Test
    void shouldOutputNoEventsFound() throws SQLException {
      whenSearchHistory();

      commandService.history(searchHistoryDto);

      verifySearchHistory();

      assertThat(LOG_INTERCEPTOR.getLoggedEvents())
          .hasSize(1)
          .element(0)
          .extracting(ILoggingEvent::getFormattedMessage)
          .isEqualTo("No events found.");
    }

    @ParameterizedTest
    @EnumSource(Status.class)
    void shouldOutputEventsFound(Status status) throws SQLException {
      ServerEvent serverEvent = createServerEvent(status);
      addEventToHistory(serverEvent);
      whenSearchHistory();

      commandService.history(searchHistoryDto);

      verifySearchHistory();

      assertThat(LOG_INTERCEPTOR.getLoggedEvents()).hasSize(serverEvents.size());
      IntStream.range(0, serverEvents.size()).forEach(this::assertLogs);
    }

    private void assertLogs(int index) {
      ServerEvent event = serverEvents.get(index);
      String expectedMessage = getExpectedLogMessage(event.getStatus(), event.getCreationTime());
      assertThat(LOG_INTERCEPTOR.getLoggedEvents())
          .element(index)
          .extracting(ILoggingEvent::getFormattedMessage)
          .isEqualTo(expectedMessage);
    }

    private String getExpectedLogMessage(Status status, LocalDateTime creationTime) {
      String formattedTime =
          creationTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
      return switch (status) {
        case UP -> String.format("Server is UP since %s", formattedTime);
        case DOWN -> String.format("Server went DOWN at %s", formattedTime);
        case STARTING -> String.format("Server is STARTING as of %s", formattedTime);
        case STOPPING -> String.format("Server is STOPPING as of %s", formattedTime);
        case FAILED -> String.format("Server FAILED at %s", formattedTime);
      };
    }
  }
}
