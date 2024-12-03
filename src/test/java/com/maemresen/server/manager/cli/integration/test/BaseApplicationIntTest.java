package com.maemresen.server.manager.cli.integration.test;

import static org.assertj.core.api.Assertions.assertThat;

import ch.qos.logback.classic.spi.ILoggingEvent;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.maemresen.server.manager.cli.beans.Application;
import com.maemresen.server.manager.cli.beans.DataSource;
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
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
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

  private static final LogInterceptor LOG_INTERCEPTOR =
      LogInterceptor.forClass(CommandService.class, Level.INFO);

  protected Injector injector;
  protected Application application;
  protected ServerEventRepository repository;
  protected DataSource dataSource;

  @BeforeAll
  static void setUp() {
    LOG_INTERCEPTOR.start();
  }

  @BeforeEach
  void setupEach(TestInfo testInfo) throws SQLException, IOException {
    Map<String, String> testProperties = new HashMap<>();
    String displayName = testInfo.getDisplayName();
    String jdbc = String.format("jdbc:h2:file:./data/%s;AUTO_SERVER=TRUE", displayName);
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
    dataSource = injector.getInstance(DataSource.class);

    dataSource.execute(getClass().getResourceAsStream("/scheme.sql"));
    executeSql("DELETE FROM PUBLIC.SERVER_EVENT WHERE 1 = 1;");
    resetLogger();
  }

  protected void resetLogger() {
    LOG_INTERCEPTOR.reset();
  }

  protected List<ILoggingEvent> getLoggedEvents() {
    return LOG_INTERCEPTOR.getLoggedEvents();
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
    assertThat(getLoggedEvents())
        .map(ILoggingEvent::getFormattedMessage)
        .containsExactly(logMessages);
  }

  protected void executeSql(String sql) throws SQLException, IOException {
    dataSource.execute(new ByteArrayInputStream(sql.getBytes(StandardCharsets.UTF_8)));
  }

  protected void createServerEvent(String creationTime, Status status)
      throws SQLException, IOException {
    String sql = "INSERT INTO PUBLIC.SERVER_EVENT (STATUS, CREATION_TIME) VALUES ('%s', '%s');";
    String finalSql = String.format(sql, status, creationTime);
    executeSql(finalSql);
  }
}
