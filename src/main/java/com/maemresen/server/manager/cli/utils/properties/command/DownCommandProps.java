package com.maemresen.server.manager.cli.utils.properties.command;

import lombok.experimental.UtilityClass;

@UtilityClass
public class DownCommandProps {
  public static final String RANDOM_WAIT_SECONDS_MIN = "command.down.random-wait-seconds.min";
  public static final String RANDOM_WAIT_SECONDS_MAX = "command.down.random-wait-seconds.max";
  public static final String FAILURE_PROBABILITY = "command.down.failure-probability";
}
