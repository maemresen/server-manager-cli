package com.maemresen.server.manager.cli.command;

public interface Command {

  String getName();

  void run(String... args) throws InterruptedException;
}
