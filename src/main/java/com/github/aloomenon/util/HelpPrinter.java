package com.github.aloomenon.util;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

public class HelpPrinter {
    
    private final static String HEADER = "Do something with serial ports" + System.lineSeparator();
    private final static String FOOTER = System.lineSeparator()
            + "Please report issues to https://github.com/Aloomenon/SerialPortAccessAdapter";
    private final static String NAME = "serialPortAdapter";
    
    private final Options options;

    public HelpPrinter(Options options) {
        this.options = options;
    }
    
    public void print() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(NAME, HEADER, options, FOOTER, true);
    }
}
