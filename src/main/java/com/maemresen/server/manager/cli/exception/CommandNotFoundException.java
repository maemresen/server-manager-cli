package com.maemresen.server.manager.cli.exception;

public class CommandNotFoundException extends RuntimeException {

  public CommandNotFoundException(String message) {
    super(message);
  }
}
