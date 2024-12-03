package com.maemresen.server.manager.cli.integration.test;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.maemresen.server.manager.cli.beans.AppProps;
import com.maemresen.server.manager.cli.beans.Application;
import com.maemresen.server.manager.cli.beans.DataSource;
import com.maemresen.server.manager.cli.beans.command.Command;
import com.maemresen.server.manager.cli.beans.command.CommandFactory;
import com.maemresen.server.manager.cli.beans.command.impl.DownCommand;
import com.maemresen.server.manager.cli.beans.command.impl.HelpCommand;
import com.maemresen.server.manager.cli.beans.command.impl.HistoryCommand;
import com.maemresen.server.manager.cli.beans.command.impl.StatusCommand;
import com.maemresen.server.manager.cli.beans.command.impl.UpCommand;
import com.maemresen.server.manager.cli.beans.repository.ServerEventRepository;
import com.maemresen.server.manager.cli.beans.service.CommandService;
import java.util.Properties;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RequiredArgsConstructor
public class TestApplicationModule extends AbstractModule {
  private final Properties props;

  @SneakyThrows
  @Override
  protected void configure() {

    bind(CommandService.class).asEagerSingleton();
    bind(ServerEventRepository.class).asEagerSingleton();
    bind(AppProps.class).toInstance(new AppProps(props));
    bind(DataSource.class).asEagerSingleton();
    bind(CommandFactory.class).asEagerSingleton();
    bind(Application.class).asEagerSingleton();

    Multibinder<Command> commandBinder = Multibinder.newSetBinder(binder(), Command.class);
    commandBinder.addBinding().to(HistoryCommand.class);
    commandBinder.addBinding().to(StatusCommand.class);
    commandBinder.addBinding().to(UpCommand.class);
    commandBinder.addBinding().to(DownCommand.class);
    commandBinder.addBinding().to(HelpCommand.class);
  }
}
