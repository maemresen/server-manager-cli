package com.maemresen.server.manager.cli;

import com.maemresen.server.manager.cli.command.CommandFactory;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServerManagerCli {
  public static void main(String[] args) throws InterruptedException {
    if (args.length == 0) {
      log.error("Available commands: status|up|down|history");
    }
    String commandName = args[0];
    String[] commandParameters = Arrays.copyOfRange(args, 1, args.length);
    CommandFactory.getCommand(commandName).run(commandParameters);
  }
}
