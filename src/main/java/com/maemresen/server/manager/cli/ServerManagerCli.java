package com.maemresen.server.manager.cli;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.maemresen.server.manager.cli.command.CommandFactory;
import com.maemresen.server.manager.cli.context.ApplicationModule;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServerManagerCli {
  public static void main(String[] args) throws InterruptedException {
    Injector injector = Guice.createInjector(new ApplicationModule());
    String commandName = args[0];
    String[] commandParameters = Arrays.copyOfRange(args, 1, args.length);
    CommandFactory factory = injector.getInstance(CommandFactory.class);
    factory.getCommand(commandName).run(commandParameters);
  }
}
