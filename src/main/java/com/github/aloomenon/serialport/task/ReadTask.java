package com.github.aloomenon.serialport.task;

import java.io.ByteArrayOutputStream;
import org.apache.log4j.Logger;
import com.fazecast.jSerialComm.SerialPort;
import com.github.aloomenon.exception.BytesWerentReadException;

public class ReadTask implements Task {

    private static final Logger LOGGER = Logger.getLogger(ReadTask.class);

    private final SerialPort port;
    private final int timeout;

    ReadTask(SerialPort port, int timeout) {
        this.port = port;
        this.timeout = timeout;
    }

    @Override
    public byte[] execute() {
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
