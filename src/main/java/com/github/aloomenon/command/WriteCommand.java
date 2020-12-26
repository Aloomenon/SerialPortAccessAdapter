package com.github.aloomenon.command;

import java.util.Arrays;
import javax.xml.bind.DatatypeConverter;
import org.apache.log4j.Logger;
import com.github.aloomenon.response.Response;
import com.github.aloomenon.response.StringResponse;
import com.github.aloomenon.serialport.SerialPortAdapter;

public class WriteCommand implements Command {

    private static final Logger LOGGER = Logger.getLogger(WriteCommand.class);

    private final SerialPortAdapter adapter;
    private final String command;

    public WriteCommand(SerialPortAdapter adapter, String command) {
        this.adapter = adapter;
        this.command = command;
    }

    @Override
    public Response execute() {
        LOGGER.info("execute Write command");
        byte[] buffer = DatatypeConverter.parseHexBinary(command);
        LOGGER.info("converted buffer:" + Arrays.toString(buffer));
        byte[] response = adapter.writeWithResponse(buffer);
        String hex = DatatypeConverter.printHexBinary(response);
        LOGGER.info("Write command complited");
        return new StringResponse(hex);
    }

}
