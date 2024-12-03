package com.maemresen.server.manager.cli.utils.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum DbProps implements Property {
  JDBC_URL("jdbc.url"),
  JDBC_USERNAME("jdbc.username"),
  JDBC_PASSWORD("jdbc.password");

  private final String key;
}
