package com.maemresen.server.manager.cli.utils;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import java.util.Collections;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class LogInterceptor {
  private final ListAppender<ILoggingEvent> appender;

  @SuppressWarnings("java:S4792")
  public static <T> LogInterceptor forClass(final Class<T> clazz, final Level level) {
    final Logger logger = (Logger) LoggerFactory.getLogger(clazz);
    final ListAppender<ILoggingEvent> testLogAppender = new ListAppender<>();
    testLogAppender.setContext((LoggerContext) LoggerFactory.getILoggerFactory());
    logger.setLevel(ch.qos.logback.classic.Level.convertAnSLF4JLevel(level));
    logger.addAppender(testLogAppender);
    return new LogInterceptor(testLogAppender);
  }

  public void start() {
    appender.start();
  }

  public void reset() {
    appender.list.clear();
  }

  public List<ILoggingEvent> getLoggedEvents() {
    return Collections.unmodifiableList(appender.list);
  }
}
