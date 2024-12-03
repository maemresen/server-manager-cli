package com.maemresen.server.manager.cli.config;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.maemresen.server.manager.cli.beans.AppProps;
import com.maemresen.server.manager.cli.beans.Application;
import com.maemresen.server.manager.cli.beans.DataSource;
import com.maemresen.server.manager.cli.beans.command.Command;
import com.maemresen.server.manager.cli.beans.command.CommandFactory;
import com.maemresen.server.manager.cli.beans.command.impl.*;
import com.maemresen.server.manager.cli.beans.repository.ServerEventRepository;
import com.maemresen.server.manager.cli.beans.service.CommandService;
import com.maemresen.server.manager.cli.utils.properties.PropertyUtils;
import java.util.Properties;
import lombok.SneakyThrows;

public class ApplicationModule extends AbstractModule {
  @SneakyThrows
  @Override
  protected void configure() {
    Properties props = PropertyUtils.loadProperties("application.properties");

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
