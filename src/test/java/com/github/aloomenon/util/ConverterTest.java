package com.github.aloomenon.util;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import com.github.aloomenon.util.converter.BinToHexConverter;
import com.github.aloomenon.util.converter.Converter;
import com.github.aloomenon.util.converter.HexToBinConverter;

public class ConverterTest {

    private final static String BIN = "something. Test text. G00d day to you bruh!";
    private final static String HEX =
            "736f6d657468696e672e205465737420746578742e20473030642064617920746f20796f75206272756821";
    private final static String EMPTY = "";

    @Test
    public void hexToBinTest() {
        Converter hex2bin = new HexToBinConverter(HEX);
        String actual = hex2bin.convert();
        assertEquals(BIN, actual);
    }

    @Test
    public void binToHexTest() {
        Converter bin2hex = new BinToHexConverter(BIN);
        String actual = bin2hex.convert();
        assertEquals(HEX, actual);
    }

    @Test
    public void hexToBinNullTest() {
        Converter hex2bin = new HexToBinConverter(null);
        String actual = hex2bin.convert();
        assertEquals(EMPTY, actual);
    }

    @Test
    public void binToHexNullTest() {
        Converter bin2hex = new BinToHexConverter(null);
        String actual = bin2hex.convert();
        assertEquals(EMPTY, actual);
    }

    @Test
    public void byteConverterTest() {
        ByteUtf8Converter converter = new ByteUtf8Converter();
        byte[] actualBytes = converter.convertToByte(BIN);
        String actualStr = converter.convertToString(actualBytes);
        assertEquals(BIN, actualStr);
        assertArrayEquals(actualBytes, converter.convertToByte(actualStr));
    }

}
