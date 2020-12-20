package com.github.aloomenon.command;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import org.apache.log4j.Logger;
import com.fazecast.jSerialComm.SerialPort;
import com.github.aloomenon.exception.BytesWerentReadException;
import com.github.aloomenon.exception.BytesWerentWrittenException;
import com.github.aloomenon.exception.NullPortException;
import com.github.aloomenon.response.Response;
import com.github.aloomenon.response.StringResponse;

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
        SerialPort commPort = SerialPort.getCommPort(port);
        commPort.openPort();

        byte[] buffer = hexToBin(command);
        int byteNum = commPort.writeBytes(buffer, buffer.length);
        if (byteNum == -1) {
            throw new BytesWerentWrittenException();
        }

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
        return new StringResponse(binToHex(baos.toByteArray()));
    }

    private byte[] hexToBin(String command) {
        LOGGER.info(String.format("Start converting '%s' to binary", command));
        char[] chars = command.toCharArray();
        StringBuilder hex = new StringBuilder();
        for (char ch : chars) {
            hex.append(Integer.toHexString((int) ch));
        }
        String hexString = hex.toString();
        byte[] binary = hexString.getBytes(StandardCharsets.UTF_8);
        LOGGER.info(String.format("Hex presentation: '%s' -> to binary '%s'", hexString, binary));
        return binary;
    }

    private String binToHex(byte[] arr) {
        String hexStr = new String(arr, StandardCharsets.UTF_8);
        StringBuilder output = new StringBuilder("");

        for (int i = 0; i < hexStr.length(); i += 2) {
            String str = hexStr.substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));
        }

        return output.toString();
    }

}
