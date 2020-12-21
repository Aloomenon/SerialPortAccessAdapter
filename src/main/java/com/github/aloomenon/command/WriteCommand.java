package com.github.aloomenon.command;

import java.io.ByteArrayOutputStream;
import java.util.Optional;
import org.apache.log4j.Logger;
import com.fazecast.jSerialComm.SerialPort;
import com.github.aloomenon.exception.BytesWerentReadException;
import com.github.aloomenon.exception.BytesWerentWrittenException;
import com.github.aloomenon.exception.NullPortException;
import com.github.aloomenon.response.Response;
import com.github.aloomenon.response.StringResponse;
import com.github.aloomenon.util.ByteUtf8Converter;
import com.github.aloomenon.util.converter.BinToHexConverter;
import com.github.aloomenon.util.converter.HexToBinConverter;

public class WriteCommand implements Command {

    private static final Logger LOGGER = Logger.getLogger(WriteCommand.class);

    private static final int TIMEOUT_MS = 5000;

    private final String port;
    private final String command;

    public WriteCommand(String port, String command) {
        this.port = port;
        this.command = command;
    }

    @Override
    public Response execute() {
        Optional.ofNullable(port).orElseThrow(
                () -> new NullPortException("You cannot write without specifying a port. Use -p"));

        ByteUtf8Converter byteConverter = new ByteUtf8Converter();

        SerialPort commPort = SerialPort.getCommPort(port);
        commPort.openPort();

        byte[] buffer = byteConverter.toByte(new HexToBinConverter(command).convert());
        int byteNum = commPort.writeBytes(buffer, buffer.length);
        if (byteNum == -1) {
            throw new BytesWerentWrittenException();
        }

        // TODO: Move to ReadCommand
        commPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 1000, 0);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            long start = System.currentTimeMillis();
            while (start + TIMEOUT_MS >= System.currentTimeMillis()) {
                byte[] readBuffer = new byte[1024];
                int numRead = commPort.readBytes(readBuffer, readBuffer.length);
                if (numRead == -1) {
                    throw new BytesWerentReadException();
                }
                baos.write(readBuffer);
            }
        } catch (Exception e) {
            LOGGER.error(e);
        }

        commPort.closePort();
        return new StringResponse(
                new BinToHexConverter(byteConverter.toString(baos.toByteArray())).convert());
    }

}
