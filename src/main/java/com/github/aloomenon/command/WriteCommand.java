package com.github.aloomenon.command;

import org.apache.log4j.Logger;
import com.github.aloomenon.response.Response;
import com.github.aloomenon.response.StringResponse;
import com.github.aloomenon.serialport.SerialPortAdapter;
import com.github.aloomenon.util.ByteUtf8Converter;
import com.github.aloomenon.util.converter.BinToHexConverter;
import com.github.aloomenon.util.converter.HexToBinConverter;

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
        ByteUtf8Converter byteConverter = new ByteUtf8Converter();

        byte[] buffer = byteConverter.convertToByte(new HexToBinConverter(command).convert());
        byte[] response = adapter.writeWithResponse(buffer);
        String hex = new BinToHexConverter(byteConverter.convertToString(response)).convert();
        LOGGER.info("Write response: " + hex);
        return new StringResponse(hex);
    }

}
