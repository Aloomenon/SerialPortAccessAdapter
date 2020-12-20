package com.github.aloomenon.serialport;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import com.fazecast.jSerialComm.SerialPort;

public class SerialPortAdapter {

    public static List<String> getCommPorts() {
        return Arrays.stream(SerialPort.getCommPorts())
                .map(SerialPort::getPortDescription)
                .collect(Collectors.toList());
    }
}
