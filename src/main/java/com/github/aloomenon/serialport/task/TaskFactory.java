package com.github.aloomenon.serialport.task;

import com.fazecast.jSerialComm.SerialPort;

public class TaskFactory {

    public Task createReadTask(SerialPort port, int timeout) {
        return new ReadTask(port, timeout);
    }

    public Task createWriteTask(SerialPort port, byte[] payload) {
        return new WriteTask(port, payload);
    }
}
