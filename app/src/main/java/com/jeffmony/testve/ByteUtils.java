package com.jeffmony.testve;

public class ByteUtils {

    public static int byteArrayToInt(byte[] b) {
        return (b[0] & 0xff) |
                ((b[1] & 0xff) << 8) |
                ((b[2] & 0xff) << 16) |
                ((b[3] & 0xff) << 24);
    }
}
