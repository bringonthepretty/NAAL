package com.wah.naal.utils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;

/**
 * This class represents util method for operations with half precision floating point numbers.
 * This is not  IEEE-754 half precision floating point standard.
 * This class is singleton
 */
public class FloatingPointConvertor {

    private static final Float MAX_2_BYTE_FLOAT_VALUE = 511.9999F;
    private static final Float MIN_2_BYTE_FLOAT_VALUE = 1E-14F;

    private static FloatingPointConvertor instance;

    /**
     * Returns instance of {@link FloatingPointConvertor}
     * @return instance of {@link FloatingPointConvertor}
     */
    public static synchronized FloatingPointConvertor getInstance() {
        if (Objects.isNull(instance)){
            instance = new FloatingPointConvertor();
        }
        return instance;
    }


    /**
     * Converts 2 bytes to float.
     * This is not IEEE-754 half precision floating point conversion.
     * Structure here is 1 bit sign, 6 bit exponent, 9 bit significant fraction with bias being 47
     * @param source source bytes
     * @return float result
     */
    public float bytesToFloat(byte[] source) {

        if (source.length < 2){
            throw new IllegalArgumentException("array must be at least 2 bytes long");
        }

        int signMultiplier;
        int exponent = 0;
        int bias = 47;
        int biasedExponent;
        BigDecimal fraction = new BigDecimal("0", MathContext.DECIMAL128).setScale(10, RoundingMode.HALF_EVEN);
        List<Integer> bits = new ArrayList<>();
        StringBuilder byte1AsBinaryString = new StringBuilder(Integer.toBinaryString(source[0] & 0xFF));
        StringBuilder byte2AsBinaryString = new StringBuilder(Integer.toBinaryString(source[1] & 0xFF));

        while (byte1AsBinaryString.length() < 8){
            byte1AsBinaryString.insert(0, 0);
        }

        while (byte2AsBinaryString.length() < 8){
            byte2AsBinaryString.insert(0, 0);
        }

        bits.addAll(Arrays.stream(byte1AsBinaryString.toString().split("\\B")).map(Integer::parseInt).toList());
        bits.addAll(Arrays.stream(byte2AsBinaryString.toString().split("\\B")).map(Integer::parseInt).toList());

        signMultiplier = (int) Math.pow(-1, bits.get(0));

        int powerCounter = 5;
        for (int i = 1; i < 7; i++){
            if (bits.get(i) == 1){
                exponent += Math.pow(2 * bits.get(i), powerCounter);
            }
            powerCounter--;
        }
        biasedExponent = exponent - bias;

        for (int i = 7; i < 16; i++) {
            BigDecimal denominator = new BigDecimal(Float.toString(bits.get(i) * 2f), MathContext.DECIMAL128)
                    .setScale(10, RoundingMode.HALF_EVEN)
                    .pow(Math.abs(powerCounter));
            powerCounter--;
            if (denominator.floatValue() == .0f){
                continue;
            }
            BigDecimal value = new BigDecimal("1.0", MathContext.DECIMAL128)
                    .setScale(10, RoundingMode.HALF_EVEN)
                    .divide(denominator, RoundingMode.HALF_EVEN);
            fraction = fraction.add(value);
        }

        return (float)(signMultiplier * Math.pow(2, biasedExponent) * (fraction.doubleValue() + 1));
    }

    /**
     * Converts float to 2 bytes.
     * This is not IEEE-754 half precision floating point conversion.
     * Structure here is 1 bit sign, 6 bit exponent, 9 bit significant fraction with bias being 47
     * @param source source float
     * @return byte array result
     */
    public byte[] floatToBytes(float source) {
        int bias = 47;
        int signBit;
        LinkedList<Integer> pureBinaryWholeNumber = new LinkedList<>();
        LinkedList<Integer> pureBinaryFraction = new LinkedList<>();
        LinkedList<Integer> exponentBits = new LinkedList<>();
        LinkedList<Integer> fractionBits = new LinkedList<>();
        LinkedList<Integer> resultBits = new LinkedList<>();
        int exponent;
        float fraction;

        signBit = source >= 0 ? 0 : 1;
        source = Math.abs(source);

        if (source > MAX_2_BYTE_FLOAT_VALUE) {
            source = MAX_2_BYTE_FLOAT_VALUE;
        }

        if (source < MIN_2_BYTE_FLOAT_VALUE) {
            return new byte[]{0, 0};
        }

        fraction = source - (int)source;

        int wholeNumber = (int)source;

        if (wholeNumber != 0){
            while (wholeNumber != 1){
                pureBinaryWholeNumber.add(wholeNumber % 2);
                wholeNumber /= 2;
            }
            pureBinaryWholeNumber.add(1);
            exponent = pureBinaryWholeNumber.size() - 1;
            Collections.reverse(pureBinaryWholeNumber);

            if (pureBinaryWholeNumber.size() > 9){
                throw new IllegalArgumentException("Value is too big");
            }

            while (fraction != 0.0f && pureBinaryFraction.size() <= 9 - pureBinaryWholeNumber.size()){
                float doubledFraction = fraction * 2;
                pureBinaryFraction.add((int)(doubledFraction - (doubledFraction - (int)doubledFraction)));
                fraction = doubledFraction - (int)doubledFraction;
            }

            fractionBits.addAll(pureBinaryWholeNumber);
        } else {
            exponent = -1;
            boolean shifted = false;

            while (fraction != 0.0f && pureBinaryFraction.size() <= 9){
                float doubledFraction = fraction * 2;
                int bitValue = (int)(doubledFraction - (doubledFraction - (int)doubledFraction));
                if (bitValue == 1 || shifted){
                    shifted = true;
                    pureBinaryFraction.add(bitValue);
                } else {
                    exponent--;
                }
                fraction = doubledFraction - (int)doubledFraction;
            }
        }

        fractionBits.addAll(pureBinaryFraction);
        fractionBits.removeFirst();
        while (fractionBits.size() < 9){
            fractionBits.addLast(0);
        }

        int biasedExponent = exponent + bias;
        StringBuilder exponentBitsAsString = new StringBuilder(Integer.toBinaryString(biasedExponent));

        if (exponentBitsAsString.length() > 6){
            //exponentBitsAsString = new StringBuilder("111111");
            throw new IllegalArgumentException("Value " + source + " is too big or too small");
        }

        while (exponentBitsAsString.length() < 6){
            exponentBitsAsString.insert(0, 0);
        }

        exponentBits.addAll(Arrays.stream(exponentBitsAsString.toString().split("\\B")).map(Integer::parseInt).toList());

        resultBits.add(signBit);
        resultBits.addAll(exponentBits);
        resultBits.addAll(fractionBits);
        Collections.reverse(resultBits);

        BitSet bitSet = new BitSet(16);

        for (int i = 0; i < resultBits.size(); i++) {
            bitSet.set(i, resultBits.get(i).equals(1));
        }

        if (bitSet.stream().allMatch(bit -> bit == 0)) {
            return new byte[]{0, 0};
        }

        return new byte[]{bitSet.toByteArray()[1], bitSet.toByteArray()[0]};
    }
}
