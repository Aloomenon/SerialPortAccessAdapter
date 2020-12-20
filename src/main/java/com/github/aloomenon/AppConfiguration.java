package com.github.aloomenon;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import com.github.aloomenon.command.Command;

public class AppConfiguration {
    
    private final String port;
    private final List<Command> commands;
    
    public AppConfiguration(String port, List<Command> commands) {
        this.port = port;
        this.commands = commands;
    }
    
    public Optional<String> getPort() {
        return Optional.ofNullable(port);
    }

    public List<Command> getCommands() {
        return commands != null ? commands : Collections.emptyList();
    }
    
}
