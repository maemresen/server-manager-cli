package com.maemresen.server.manager.cli.beans;

import com.maemresen.server.manager.cli.utils.properties.Property;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AppProps {

  private final Properties props;

  public Optional<String> getProp(Property key) {
    return getProp(key, Function.identity());
  }

  public Optional<Integer> getIntProp(Property key) {
    return getProp(key, Integer::parseInt);
  }

  public <T> Optional<T> getProp(Property key, Function<String, T> valueMapper) {
    String property = props.getProperty(key.getKey());
    return Optional.ofNullable(property).map(valueMapper);
  }
}
