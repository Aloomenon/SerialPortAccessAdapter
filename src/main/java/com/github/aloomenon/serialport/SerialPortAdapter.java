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

    private final SerialPort port;

    public SerialPortAdapter(String descriptor) {
        this.port = getSerialPort(descriptor);
    }

    public static List<String> getCommPorts() {
        return Arrays.stream(SerialPort.getCommPorts()).map(SerialPort::getSystemPortName)
                .collect(Collectors.toList());
    }

    // TODO: move to Command calls.
    public byte[] executeRead() {
        port.openPort();
        byte[] bytes;
        try {
            bytes = read(500);
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

    public byte[] writeWithResponse(byte[] data, int timeout) {
        port.openPort();

        setParameters(115200, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
        byte[] response;
        try {
            write(data);
            response = read(timeout);
        } finally {
            port.closePort();
        }

        return response;
    }

    protected SerialPort getSerialPort(String handle) {
        return SerialPort.getCommPort(Optional.ofNullable(handle).orElseThrow(
                () -> new NullPortException("You cannot write without specifying a port. Use -p")));
    }

    private void setParameters(int baudRate, int dataBits, int stopBit, int parity) {
        if (!port.isOpen()) {
            throw new RuntimeException("Port didn't open");
        }
        port.setComPortParameters(115200, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
    }

    private void write(byte[] data) {
        LOGGER.info("Start writing to port");
        int byteNum = port.writeBytes(data, data.length);
        if (byteNum == -1) {
            throw new BytesWerentWrittenException();
        }
        LOGGER.info(byteNum + " bytes were written to port");
    }

    private byte[] read(int timeout) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        LOGGER.info("Start reading from port " + port.getSystemPortName());
        try {
            long start = System.currentTimeMillis();
            while (start + timeout >= System.currentTimeMillis()) {
                if (port.bytesAvailable() <= 0) {
                    continue;
                }
                byte[] readBuffer = new byte[port.bytesAvailable()];
                int numRead = port.readBytes(readBuffer, readBuffer.length);
                if (numRead == -1) {
                    throw new BytesWerentReadException();
                }
                LOGGER.info(String.format("%s bytes were read from %s", numRead,
                        port.getSystemPortName()));
                if (numRead > 0) {
                    start = System.currentTimeMillis();
                    baos.write(readBuffer);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error occured while reading", e);
        }
        byte[] result = baos.toByteArray();
        LOGGER.info(String.format("Finish reading from port. %s bytes were read.", result.length));
        return result;
    }
}
