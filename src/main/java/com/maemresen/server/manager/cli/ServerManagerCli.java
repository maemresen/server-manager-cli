package com.maemresen.server.manager.cli;

import com.maemresen.server.manager.cli.model.dto.SearchHistoryDto;
import com.maemresen.server.manager.cli.service.CommandService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServerManagerCli {
  public static void main(String[] args) throws Exception {
    final var commandService = new CommandService();
    final var databaseConnector = DatabaseConnection.getInstance();
    databaseConnector.executeFile("/scheme.sql");
    commandService.up();
    commandService.up();
    commandService.status();
    commandService.down();
    commandService.down();
    commandService.history(new SearchHistoryDto());
  }
}
