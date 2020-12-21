package com.github.aloomenon.util.converter;

import java.util.Optional;
import org.apache.log4j.Logger;

/**
 * PHP hex2bin method implementation
 * 
 * @author Aloomenon
 *
 */
public class HexToBinConverter implements Converter {

    private static final Logger LOGGER = Logger.getLogger(HexToBinConverter.class);

    private final String hex;

    public HexToBinConverter(String hex) {
        this.hex = Optional.ofNullable(hex).orElse("");
    }

    @Override
    public String convert() {
        LOGGER.info(String.format("Start converting '%s' to binary", hex));
        StringBuilder output = new StringBuilder("");

        for (int i = 0; i < hex.length(); i += 2) {
            String str = hex.substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));
        }

        String bin = output.toString();
        LOGGER.info(String.format("Bin presentation: '%s'", bin));
        return bin;
    }

}
