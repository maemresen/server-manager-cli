package com.maemresen.server.manager.cli.utils.properties.command;

import com.maemresen.server.manager.cli.utils.properties.Property;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum UpCommandProps implements Property {
  RANDOM_WAIT_SECONDS_MIN("command.up.random-wait-seconds.min"),
  RANDOM_WAIT_SECONDS_MAX("command.up.random-wait-seconds.max"),
  FAILURE_PROBABILITY("command.up.failure-probability");
  private final String key;
}
