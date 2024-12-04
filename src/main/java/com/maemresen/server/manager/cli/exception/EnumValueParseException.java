package com.maemresen.server.manager.cli.exception;

import org.apache.commons.cli.ParseException;

public class EnumValueParseException extends ParseException {
  public EnumValueParseException(String parameter) {
    super("Failed to parse enum value for option: %s.".formatted(parameter));
  }
}
