package com.maemresen.server.manager.cli.context;

import com.google.inject.Guice;
import com.google.inject.Injector;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApplicationContext {

  private static final Injector INJECTOR = Guice.createInjector(new ApplicationModule());

  public static Injector getInjector() {
    return INJECTOR;
  }
}
