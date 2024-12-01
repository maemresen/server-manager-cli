package com.maemresen.server.manager.cli.command;

import com.maemresen.server.manager.cli.service.CommandService;
import java.sql.SQLException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractCommand implements Command {

  private final HelpFormatter helpFormatter = new HelpFormatter();
  private final Options options = createOptions();

  @Getter private final String name;

  protected final CommandService commandService;

  protected Options createOptions() {
    return new Options();
  }

  @Override
  public void run(String... args) throws InterruptedException {
    CommandLineParser parser = new DefaultParser();
    try {
      CommandLine cmd = parser.parse(options, args);
      handleCommandLine(cmd);
    } catch (InterruptedException interruptedException) {
      throw interruptedException;
    } catch (Exception exception) {
      log.debug(exception.getMessage(), exception);
      printHelp();
    }
  }

  protected void printHelp() {
    helpFormatter.printHelp(name, options);
  }

  protected abstract void handleCommandLine(CommandLine cmd)
      throws InterruptedException, SQLException;
}
