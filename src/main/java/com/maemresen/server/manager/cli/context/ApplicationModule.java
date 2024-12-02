package com.maemresen.server.manager.cli.context;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.maemresen.server.manager.cli.command.Command;
import com.maemresen.server.manager.cli.command.CommandFactory;
import com.maemresen.server.manager.cli.command.impl.DownCommand;
import com.maemresen.server.manager.cli.command.impl.HelpCommand;
import com.maemresen.server.manager.cli.command.impl.HistoryCommand;
import com.maemresen.server.manager.cli.command.impl.StatusCommand;
import com.maemresen.server.manager.cli.command.impl.UpCommand;
import com.maemresen.server.manager.cli.repository.ServerEventRepository;
import com.maemresen.server.manager.cli.service.CommandService;

public class ApplicationModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(CommandService.class).asEagerSingleton();
    bind(ServerEventRepository.class).asEagerSingleton();
    bind(CommandFactory.class).asEagerSingleton();

    Multibinder<Command> commandBinder = Multibinder.newSetBinder(binder(), Command.class);
    commandBinder.addBinding().to(HistoryCommand.class);
    commandBinder.addBinding().to(StatusCommand.class);
    commandBinder.addBinding().to(UpCommand.class);
    commandBinder.addBinding().to(DownCommand.class);
    commandBinder.addBinding().to(HelpCommand.class);
  }
}
