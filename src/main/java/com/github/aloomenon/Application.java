package com.github.aloomenon;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import com.github.aloomenon.cli.CliParser;
import com.github.aloomenon.command.Command;

public class Application {

    private static final Logger LOGGER = Logger.getLogger(Application.class);

    public static void main(String... args) {
        BasicConfigurator.configure();
        CliParser parser = new CliParser(args);
        try {
            AppConfiguration configuration = parser.parse();
            configuration.getCommands().stream().map(Command::execute)
                    .forEach(response -> LOGGER.info(response.getResponse()));
        } catch (Exception e) {
            LOGGER.error(e);
        }
    }

}
