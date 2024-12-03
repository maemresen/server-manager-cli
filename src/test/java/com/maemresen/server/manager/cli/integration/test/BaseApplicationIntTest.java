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
import java.sql.SQLException;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Stream;
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
    Map<String, String> additionalProps = new HashMap<>();
    testInfo
        .getTestMethod()
        .map(method -> method.getDeclaredAnnotation(TestProperty.class))
        .map(Stream::of)
        .orElseGet(Stream::empty)
        .forEach(testProperty -> additionalProps.put(testProperty.key(), testProperty.value()));

    Properties properties = new Properties();
    String testDbJdbcUrl = "jdbc:h2:mem:test_%s;DB_CLOSE_DELAY=-1".formatted(Instant.now());
    properties.setProperty(DbProps.JDBC_URL, testDbJdbcUrl);
    properties.setProperty(DbProps.JDBC_USERNAME, "sa");
    additionalProps.forEach(additionalProps::put);
    injector = Guice.createInjector(new TestApplicationModule(properties));
    application = injector.getInstance(Application.class);
    repository = injector.getInstance(ServerEventRepository.class);
    LOG_INTERCEPTOR.reset();
  }

  protected List<ServerEvent> searchServerEvents() throws SQLException {
    SearchHistoryDto searchHistoryDto = new SearchHistoryDto();
    searchHistoryDto.setSort(Sort.ASC);
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
