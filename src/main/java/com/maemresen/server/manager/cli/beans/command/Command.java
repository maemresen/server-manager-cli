package com.maemresen.server.manager.cli.beans.command;

public interface Command {

  String getName();

  void run(String... args) throws InterruptedException;
}
