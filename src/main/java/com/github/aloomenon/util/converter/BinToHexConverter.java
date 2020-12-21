package com.github.aloomenon.util.converter;

import java.util.Optional;
import org.apache.log4j.Logger;

/**
 * PHP bin2hex method implementation
 * 
 * @author Aloomenon
 *
 */
public class BinToHexConverter implements Converter {

    private static final Logger LOGGER = Logger.getLogger(BinToHexConverter.class);

    private final String bin;

    public BinToHexConverter(String bin) {
        this.bin = Optional.ofNullable(bin).orElse("");
    }

    @Override
    public String convert() {
        LOGGER.info(String.format("Start converting '%s' to hex", bin));
        char[] chars = bin.toCharArray();
        StringBuilder hex = new StringBuilder();
        for (char ch : chars) {
            hex.append(Integer.toHexString((int) ch));
        }
        String hexStr = hex.toString();
        LOGGER.info(String.format("Hex presentation: '%s'", hexStr));
        return hexStr;
    }

}
