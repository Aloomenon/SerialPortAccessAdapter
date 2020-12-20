package com.github.aloomenon;

import org.apache.commons.cli.ParseException;
import org.apache.log4j.Logger;
import com.github.aloomenon.cli.CliParser;

public class SerialPortAdapter {
    
    private static final Logger LOGGER = Logger.getLogger(SerialPortAdapter.class);

    public static void main(String... args) {
        CliParser parser = new CliParser(args);
        try {
            AppConfiguration configuration = parser.parse();
        } catch (ParseException e) {
            LOGGER.error(e);
        }
    }

}
