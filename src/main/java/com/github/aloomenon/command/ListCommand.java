package com.github.aloomenon.command;

import java.util.List;
import java.util.stream.Collectors;
import com.github.aloomenon.response.StringResponse;
import com.github.aloomenon.serialport.SerialPortAdapter;

public class ListCommand implements Command {

    @Override
    public StringResponse execute() {
        List<String> commPorts = SerialPortAdapter.getCommPorts();
        if (commPorts.isEmpty()) {
            return new StringResponse("Ports not found");
        }
        return new StringResponse(commPorts.stream().collect(Collectors
                .joining(System.lineSeparator(), "Ports available: ", System.lineSeparator())));
    }

}
