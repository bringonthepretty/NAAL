package com.wah.naal.utils;

public class HexStringUtils {
    /**
     * Returns a string representation of the integer argument as an unsigned integer in base 16 by byte.
     * <p> 10 in base 10 with byte amount 1 will be converted to 0A.
     * <p> 10 in base 10 with byte amount 2 will be converted to 000A.
     * <p> 538051589 in base 10 with byte amount 2 will be converted to 0504.
     * <p> 538051589 in base 10 with byte amount 4 will be converted to 05041220.
     * @param value an integer to be converted to a string
     * @param byteCount count of bytes
     * @return a string representation of the integer argument as an unsigned integer in base 16
     */
    public static String toHexString(int value, int byteCount) {

        if (byteCount < 1 || byteCount > 4){
            throw new IllegalArgumentException("Byte count cannot be less than 1 or greater than 4");
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < byteCount; i++) {
            String byteAsHex = Integer.toHexString(value >> (i * 8) & 0xFF);
            if (byteAsHex.length() < 2){
                stringBuilder.append(0);
            }
            stringBuilder.append(byteAsHex);
        }

        return stringBuilder.toString().toUpperCase();
    }

    /**
     * Returns a string representation of the long argument as an unsigned long in base 16 by byte.
     * <p> 10L in base 10 with byte amount 1 will be converted to 0A.
     * <p> 10L in base 10 with byte amount 2 will be converted to 000A.
     * <p> 538051589L in base 10 with byte amount 2 will be converted to 0504.
     * <p> 538051589L in base 10 with byte amount 4 will be converted to 05041220.
     * @param value a long to be converted to a string
     * @param byteCount count of bytes
     * @return a string representation of the long argument as an unsigned long in base 16
     */
    public static String toHexString(long value, int byteCount) {

        if (byteCount < 1 || byteCount > 8){
            throw new IllegalArgumentException("Byte count cannot be less than 1 or greater than 8");
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < byteCount; i++) {
            String byteAsHex = Long.toHexString(value >> (i * 8) & 0xFF);
            if (byteAsHex.length() < 2){
                stringBuilder.append(0);
            }
            stringBuilder.append(byteAsHex);
        }

        return stringBuilder.toString().toUpperCase();
    }
}
