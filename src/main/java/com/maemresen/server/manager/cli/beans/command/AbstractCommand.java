package com.maemresen.server.manager.cli.beans.command;

import com.maemresen.server.manager.cli.beans.service.CommandService;
import com.maemresen.server.manager.cli.utils.CmdUtils;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.Optional;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.*;

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
        logHelp();
      } else {
        handleCommandLine(cmd);
      }
    } catch (InterruptedException interruptedException) {
      throw interruptedException;
    } catch (ParseException e) {
      log.error(e.getMessage());
      logHelp();
    } catch (Exception exception) {
      log.error(exception.getMessage(), exception);
      logHelp();
    }
  }

  public void logHelp() {
    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);
    helpFormatter.printHelp(printWriter, 120, name, null, options, 2, 4, null);
    printWriter.flush();
    log.info("\n{}", stringWriter);
  }

  protected abstract void handleCommandLine(CommandLine cmd)
      throws InterruptedException, SQLException, ParseException;
}
