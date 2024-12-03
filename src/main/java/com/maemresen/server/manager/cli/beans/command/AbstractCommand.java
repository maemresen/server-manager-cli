package com.maemresen.server.manager.cli.beans.command;

import com.maemresen.server.manager.cli.beans.service.CommandService;
import com.maemresen.server.manager.cli.utils.CmdUtils;
import java.sql.SQLException;
import java.util.Optional;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

@Slf4j
public abstract class AbstractCommand implements Command {

  private final HelpFormatter helpFormatter = new HelpFormatter();

  @Getter private final Options options;
  @Getter private final String name;

  protected final CommandService commandService;

  protected AbstractCommand(String name, CommandService commandService) {
    this.name = name;
    this.commandService = commandService;
    this.options = new Options();
    this.options.addOption("h", "help", false, "help");
    configureOptions(options);
  }

  protected void configureOptions(Options options) {
    // no-additional config
  }

  @Override
  public void run(String... args) throws InterruptedException {
    CommandLineParser parser = new DefaultParser();
    try {
      CommandLine cmd = parser.parse(options, args);
      Optional<Boolean> help = CmdUtils.getBooleanParameter(cmd, "h");
      if (help.isPresent()) {
        printHelp();
      } else {
        handleCommandLine(cmd);
      }
    } catch (InterruptedException interruptedException) {
      throw interruptedException;
    } catch (Exception exception) {
      log.error(exception.getMessage(), exception);
      printHelp();
    }
  }

  protected void printHelp() {
    helpFormatter.printHelp(name, options);
  }

  protected abstract void handleCommandLine(CommandLine cmd)
      throws InterruptedException, SQLException, ParseException;
}
