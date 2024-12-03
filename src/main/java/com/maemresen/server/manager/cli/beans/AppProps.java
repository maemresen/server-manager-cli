package com.maemresen.server.manager.cli.beans;

import java.util.Optional;
import java.util.Properties;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AppProps {

  private final Properties props;

  public Optional<String> getProp(String key) {
    return getProp(key, Function.identity());
  }

  public Optional<Integer> getIntProp(String key) {
    return getProp(key, Integer::parseInt);
  }

  public <T> Optional<T> getProp(String key, Function<String, T> valueMapper) {
    String property = props.getProperty(key);
    return Optional.ofNullable(property).map(valueMapper);
  }
}
