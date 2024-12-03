package com.maemresen.server.manager.cli.utils.properties.command;

import com.maemresen.server.manager.cli.utils.properties.Property;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum DownCommandProps implements Property {
  RANDOM_WAIT_SECONDS_MIN("command.down.random-wait-seconds.min"),
  RANDOM_WAIT_SECONDS_MAX("command.down.random-wait-seconds.max"),
  FAILURE_PROBABILITY("command.down.failure-probability");
  private final String key;
}
