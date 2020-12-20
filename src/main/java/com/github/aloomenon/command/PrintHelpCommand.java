package com.github.aloomenon.command;

import org.apache.commons.cli.Options;
import com.github.aloomenon.response.Response;
import com.github.aloomenon.response.StringResponse;
import com.github.aloomenon.util.HelpPrinter;

public class PrintHelpCommand implements Command {

    private final Options options;

    public PrintHelpCommand(Options options) {
        this.options = options;
    }

    @Override
    public Response execute() {
        new HelpPrinter(options).print();
        return new StringResponse("OK");
    }

}
