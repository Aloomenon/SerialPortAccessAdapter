package com.github.aloomenon.util;

import java.nio.charset.StandardCharsets;

public class ByteUtf8Converter {

    public byte[] toByte(String command) {
        if (command == null) {
            return new byte[0];
        }
        return command.getBytes(StandardCharsets.UTF_8);
    }

    public String toString(byte[] bytes) {
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
