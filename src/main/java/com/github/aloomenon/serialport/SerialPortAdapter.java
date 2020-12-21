package com.github.aloomenon.serialport;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import com.fazecast.jSerialComm.SerialPort;
import com.github.aloomenon.exception.BytesWerentReadException;
import com.github.aloomenon.exception.BytesWerentWrittenException;
import com.github.aloomenon.exception.NullPortException;

public class SerialPortAdapter {

    private static final Logger LOGGER = Logger.getLogger(SerialPortAdapter.class);

    private static final int TIMEOUT_MS = 5000;

    private final SerialPort port;

    public SerialPortAdapter(String descriptor) {
        this.port = getSerialPort(descriptor);
    }

    public static List<String> getCommPorts() {
        return Arrays.stream(SerialPort.getCommPorts()).map(SerialPort::getPortDescription)
                .collect(Collectors.toList());
    }

    // TODO: move to Command calls.
    public byte[] executeRead() {
        port.openPort();
        byte[] bytes;
        try {
            bytes = read();
        } finally {
            port.closePort();
        }

        return bytes;
    }

    public void executeWrite(byte[] data) {
        port.openPort();
        try {
            write(data);
        } finally {
            port.closePort();
        }
    }

    public byte[] writeWithResponse(byte[] data) {
        byte[] response;
        port.openPort();
        try {
            write(data);
            response = read();
        } finally {
            port.closePort();
        }
        return response;
    }

    protected SerialPort getSerialPort(String handle) {
        return SerialPort.getCommPort(Optional.ofNullable(handle).orElseThrow(
                () -> new NullPortException("You cannot write without specifying a port. Use -p")));
    }

    private void write(byte[] data) {
        LOGGER.info("Start writing to port");
        port.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 1000, 0);
        int byteNum = port.writeBytes(data, data.length);
        if (byteNum == -1) {
            throw new BytesWerentWrittenException();
        }
        LOGGER.info(byteNum + " bytes were written to port");
    }

    private byte[] read() {
        port.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 1000, 0);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        LOGGER.info("Start reading from port");
        try {
            long start = System.currentTimeMillis();
            while (start + TIMEOUT_MS >= System.currentTimeMillis()) {
                byte[] readBuffer = new byte[1024];
                int numRead = port.readBytes(readBuffer, readBuffer.length);
                if (numRead == -1) {
                    throw new BytesWerentReadException();
                }
                LOGGER.info(numRead + " bytes were read");
                baos.write(readBuffer);
            }
        } catch (Exception e) {
            LOGGER.error(e);
        }
        LOGGER.info("Finish reading from port");
        return baos.toByteArray();
    }
}
