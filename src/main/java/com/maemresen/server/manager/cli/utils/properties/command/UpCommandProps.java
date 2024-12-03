package com.maemresen.server.manager.cli.utils.properties.command;

import lombok.experimental.UtilityClass;

@UtilityClass
public class UpCommandProps {
  public static final String RANDOM_WAIT_SECONDS_MIN = "command.up.random-wait-seconds.min";
  public static final String RANDOM_WAIT_SECONDS_MAX = "command.up.random-wait-seconds.max";
  public static final String FAILURE_PROBABILITY = "command.up.failure-probability";
}
