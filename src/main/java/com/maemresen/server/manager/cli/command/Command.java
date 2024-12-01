package com.maemresen.server.manager.cli.command;

public interface Command {
  void run(String... args) throws InterruptedException;
}
