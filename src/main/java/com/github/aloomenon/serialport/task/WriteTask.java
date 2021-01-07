package com.github.aloomenon.serialport.task;

import com.fazecast.jSerialComm.SerialPort;
import com.github.aloomenon.exception.BytesWerentWrittenException;

public class WriteTask implements Task {

    private final byte[] payload;
    private final SerialPort port;

    WriteTask(SerialPort port, byte[] payload) {
        this.payload = payload;
        this.port = port;
    }

    @Override
    public byte[] execute() {
        int byteNum = port.writeBytes(payload, payload.length);
        if (byteNum == -1) {
            throw new BytesWerentWrittenException();
        }
        return new byte[0];
    }

}
