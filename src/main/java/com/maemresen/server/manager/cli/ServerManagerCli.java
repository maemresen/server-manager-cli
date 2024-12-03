package com.maemresen.server.manager.cli;

import com.google.inject.Guice;
import com.maemresen.server.manager.cli.beans.Application;
import com.maemresen.server.manager.cli.config.ApplicationModule;
import java.io.IOException;
import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServerManagerCli {
  public static void main(String[] args) throws InterruptedException, SQLException, IOException {
    Guice.createInjector(new ApplicationModule()).getInstance(Application.class).run(args);
  }
}
