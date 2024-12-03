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
import com.maemresen.server.manager.cli.utils.LogInterceptor;
import com.maemresen.server.manager.cli.utils.properties.DbProps;
import com.maemresen.server.manager.cli.utils.properties.command.DownCommandProps;
import com.maemresen.server.manager.cli.utils.properties.command.UpCommandProps;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestInfo;
import org.slf4j.event.Level;

@Tag("int-test")
public abstract class BaseApplicationIntTest {

  protected static final LogInterceptor LOG_INTERCEPTOR =
      LogInterceptor.forClass(CommandService.class, Level.INFO);

  protected Injector injector;
  protected Application application;
  protected ServerEventRepository repository;

  @BeforeAll
  static void setUp() {
    LOG_INTERCEPTOR.start();
  }

  @BeforeEach
  void setupEach(TestInfo testInfo) {
    String jdbc =
        "jdbc:h2:file:./data/test-server-db-%s;AUTO_SERVER=TRUE"
            .formatted(testInfo.getDisplayName());
    Map<String, String> testProperties = new HashMap<>();
    testProperties.put(DbProps.JDBC_URL, jdbc);
    testProperties.put(DbProps.JDBC_USERNAME, "sa");
    testProperties.put(UpCommandProps.RANDOM_WAIT_SECONDS_MIN, "0");
    testProperties.put(UpCommandProps.RANDOM_WAIT_SECONDS_MAX, "0");
    testProperties.put(UpCommandProps.FAILURE_PROBABILITY, "0");
    testProperties.put(DownCommandProps.RANDOM_WAIT_SECONDS_MIN, "0");
    testProperties.put(DownCommandProps.RANDOM_WAIT_SECONDS_MAX, "0");
    testProperties.put(DownCommandProps.FAILURE_PROBABILITY, "0");

    testInfo
        .getTestMethod()
        .map(method -> method.getDeclaredAnnotationsByType(TestProperty.class))
        .stream()
        .flatMap(Stream::of)
        .forEach(testProperty -> testProperties.put(testProperty.key(), testProperty.value()));

    Properties properties = new Properties();
    testProperties.forEach(properties::setProperty);

    injector = Guice.createInjector(new TestApplicationModule(properties));
    application = injector.getInstance(Application.class);
    repository = injector.getInstance(ServerEventRepository.class);
    resetLogger();
  }

  @AfterEach
  void wrapUp() throws SQLException {
    repository.cleanupAllEvents();
  }

  protected void resetLogger() {
    LOG_INTERCEPTOR.reset();
  }

  protected List<ServerEvent> searchServerEvents() throws SQLException {
    return searchServerEvents(Sort.ASC);
  }

  protected List<ServerEvent> searchServerEvents(Sort sort) throws SQLException {
    SearchHistoryDto searchHistoryDto = new SearchHistoryDto();
    searchHistoryDto.setSort(sort);
    return repository.searchHistory(searchHistoryDto);
  }

  protected void assertStatuses(Status... statuses) throws SQLException {
    assertThat(searchServerEvents()).map(ServerEvent::getStatus).containsExactly(statuses);
  }

  protected void assertLogMessages(String... logMessages) {
    assertThat(LOG_INTERCEPTOR.getLoggedEvents())
        .map(ILoggingEvent::getFormattedMessage)
        .containsExactly(logMessages);
  }
}
