package com.github.aloomenon.cli;

import static com.github.aloomenon.cli.CliOption.DEBUG;
import static com.github.aloomenon.cli.CliOption.HELP;
import static com.github.aloomenon.cli.CliOption.LIST;
import static com.github.aloomenon.cli.CliOption.PORT;
import static com.github.aloomenon.cli.CliOption.RESPONSE_TIMEOUT;
import static com.github.aloomenon.cli.CliOption.WRITE;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import com.github.aloomenon.AppConfiguration;
import com.github.aloomenon.command.Command;
import com.github.aloomenon.command.ListCommand;
import com.github.aloomenon.command.PrintHelpCommand;
import com.github.aloomenon.command.WriteCommand;
import com.github.aloomenon.serialport.SerialPortAdapter;
import com.github.aloomenon.util.HelpPrinter;

public class CliParser {

    private static final Logger LOGGER = Logger.getLogger(CliParser.class);
    private static final int DEFAULT_TIMEOUT_MS = 50;

    private final String[] arguments;

    public CliParser(String[] arguments) {
        this.arguments = arguments;
    }

    public AppConfiguration parse() throws Exception {
        Options options = prepareOptions();
        try {
            if (arguments.length == 0) {
                throw new ParseException("Zero arguments passed.");
            }
            return parseOptions(options, arguments);
        } catch (Exception ex) {
            printHelp(options);
            throw ex;
        }
    }

    private AppConfiguration parseOptions(Options options, String[] arguments)
            throws ParseException {
        CommandLineParser parser = new DefaultParser();
        List<Command> commands = new ArrayList<>();
        String port = null;

        CommandLine line = parser.parse(options, arguments, false);

        if (line.hasOption(HELP.getOpt())) {
            commands.add(new PrintHelpCommand(options));
        }

        if (line.hasOption(LIST.getOpt())) {
            commands.add(new ListCommand());
        }

        if (line.hasOption(DEBUG.getOpt())) {
            applyDebugMode();
        }

        if (line.hasOption(PORT.getOpt())) {
            port = line.getOptionValue(PORT.getOpt());

            SerialPortAdapter adapter = new SerialPortAdapter(port);
            if (line.hasOption(WRITE.getOpt())) {
                Integer timeout =
                        Optional.ofNullable(line.getOptionValue(RESPONSE_TIMEOUT.getOpt()))
                                .map(Integer::parseInt).orElse(DEFAULT_TIMEOUT_MS);
                LOGGER.debug(String.format("Timeout set to %s ms.", timeout));
                commands.add(new WriteCommand(adapter, line.getOptionValue(WRITE.getOpt()), timeout));
            }
        }

        return new AppConfiguration(port, commands);
    }

    private Options prepareOptions() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();

        group.addOption(LIST.build());
        group.addOption(PORT.getBuilder().argName("PORT").build());
        options.addOptionGroup(group);

        options.addOption(WRITE.getBuilder().hasArgs().argName("Commands").build());
        options.addOption(RESPONSE_TIMEOUT.getBuilder().argName("timeout").build());

        options.addOption(DEBUG.build());
        options.addOption(HELP.build());
        return options;
    }


    private void printHelp(Options options) {
        new HelpPrinter(options).print();
    }

    private void applyDebugMode() {
        Properties props = new Properties();
        try (InputStream configStream = getClass().getResourceAsStream("/log4j-debug.properties")) {
            props.load(configStream);
        } catch (IOException e) {
            LOGGER.error("Cannot laod debug configuration file");
        }
        LogManager.resetConfiguration();
        PropertyConfigurator.configure(props);
    }
}
