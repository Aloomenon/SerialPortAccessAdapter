package com.github.aloomenon.cli;

import org.apache.commons.cli.Option;

public enum CliOption {
    HELP("h", "help", false, "Print help"),
    LIST("l", "list", false, "list all available ports"),
    PORT("p", "port", true, "Port used for I/O operations"),
    WRITE("w", "write", true, "Commands that will be written to the port");

    
    private final String opt;
    private final String longOpt;
    private final boolean hasArg;
    private final String description;
    
    CliOption(String opt, String longOpt, boolean hasArg, String description) {
        this.opt = opt;
        this.longOpt = longOpt;
        this.hasArg = hasArg;
        this.description = description;
    }

    public String getOpt() {
        return opt;
    }

    public Option build() {
        return new Option(opt, longOpt, hasArg, description);
    }
    
    public Option.Builder getBuilder() {
        Option.Builder builder = isNullOrEmpty(opt) ? Option.builder() : Option.builder(opt);
        
        if (!isNullOrEmpty(longOpt)) {
            builder.longOpt(longOpt);
        }
        
        if (hasArg) {
            builder.hasArg();
        }
        
        if (!isNullOrEmpty(description)) {
            builder.desc(description);
        }
        
        return builder;
    }
    
    private boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }
}
