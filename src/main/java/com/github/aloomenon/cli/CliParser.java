package com.github.aloomenon.cli;

import static com.github.aloomenon.cli.CliOption.HELP;
import static com.github.aloomenon.cli.CliOption.LIST;
import static com.github.aloomenon.cli.CliOption.PORT;
import static com.github.aloomenon.cli.CliOption.WRITE;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.Logger;
import com.github.aloomenon.AppConfiguration;
import com.github.aloomenon.command.Command;
import com.github.aloomenon.command.ListCommand;
import com.github.aloomenon.command.PrintHelpCommand;
import com.github.aloomenon.command.WriteCommand;
import com.github.aloomenon.serialport.SerialPortAdapter;
import com.github.aloomenon.util.HelpPrinter;

public class CliParser {

    private final static Logger LOGGER = Logger.getLogger(CliParser.class);

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

        if (line.hasOption(PORT.getOpt())) {
            port = line.getOptionValue(PORT.getOpt());

            SerialPortAdapter adapter = new SerialPortAdapter(port);
            if (line.hasOption(WRITE.getOpt())) {
                commands.add(new WriteCommand(adapter, line.getOptionValue(WRITE.getOpt())));
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

        options.addOption(HELP.build());
        return options;
    }


    private void printHelp(Options options) {
        new HelpPrinter(options).print();
    }
}
