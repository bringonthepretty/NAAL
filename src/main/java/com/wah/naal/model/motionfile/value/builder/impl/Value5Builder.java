package com.wah.naal.model.motionfile.value.builder.impl;

import com.wah.naal.model.motionfile.value.impl.Value6;
import com.wah.naal.utils.FloatingPointConvertor;
import com.wah.naal.model.motionfile.value.builder.api.ValueBuilder;
import com.wah.naal.model.motionfile.value.impl.Value5;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents builder for {@link Value5} and {@link Value6}
 */
public class Value5Builder implements ValueBuilder<Value5> {

    private final FloatingPointConvertor floatingPointConvertor = FloatingPointConvertor.getInstance();

    /**
     * Builds {@link Value5} or {@link Value6} depending on precision from source bytes
     * @param sourceBytes source bytes
     * @param offset start of value's bytes
     * @param entriesCount count of value's entries
     * @param halfPrecision data precision
     * @return {@link Value5} or {@link Value6} object
     */
    @Override
    public Value5 build(List<Byte> sourceBytes, int offset, int entriesCount, boolean halfPrecision) {
        List<Value5.Entry> entries = new ArrayList<>(entriesCount);
        int currentOffset = offset;
        float value;
        float valueDelta;
        float incomingDerivativeValue;
        float incomingDerivativeValueDelta;
        float outgoingDerivativeValue;
        float outgoingDerivativeValueDelta;

        if (!halfPrecision) {
            value = ByteBuffer.wrap(new byte[]{sourceBytes.get(offset + 3), sourceBytes.get(offset + 2), sourceBytes.get(offset + 1), sourceBytes.get(offset + 0)}).getFloat();
            valueDelta = ByteBuffer.wrap(new byte[]{sourceBytes.get(offset + 7), sourceBytes.get(offset + 6), sourceBytes.get(offset + 5), sourceBytes.get(offset + 4)}).getFloat();
            incomingDerivativeValue = ByteBuffer.wrap(new byte[]{sourceBytes.get(offset + 11), sourceBytes.get(offset + 10), sourceBytes.get(offset + 9), sourceBytes.get(offset + 8)}).getFloat();
            incomingDerivativeValueDelta = ByteBuffer.wrap(new byte[]{sourceBytes.get(offset + 15), sourceBytes.get(offset + 14), sourceBytes.get(offset + 13), sourceBytes.get(offset + 12)}).getFloat();
            outgoingDerivativeValue = ByteBuffer.wrap(new byte[]{sourceBytes.get(offset + 19), sourceBytes.get(offset + 18), sourceBytes.get(offset + 17), sourceBytes.get(offset + 16)}).getFloat();
            outgoingDerivativeValueDelta = ByteBuffer.wrap(new byte[]{sourceBytes.get(offset + 23), sourceBytes.get(offset + 22), sourceBytes.get(offset + 21), sourceBytes.get(offset + 20)}).getFloat();
            currentOffset += 24;

            for (int i = 0; i < entriesCount; i++) {
                entries.add(buildEntry(sourceBytes.subList(currentOffset, currentOffset + 8), false));
                currentOffset+=8;
            }
            return new Value5(value, valueDelta, incomingDerivativeValue, incomingDerivativeValueDelta, outgoingDerivativeValue, outgoingDerivativeValueDelta, offset, entries);
        } else {
            value = floatingPointConvertor.bytesToFloat(new byte[]{sourceBytes.get(offset + 1), sourceBytes.get(offset + 0)});
            valueDelta = floatingPointConvertor.bytesToFloat(new byte[]{sourceBytes.get(offset + 3), sourceBytes.get(offset + 2)});
            incomingDerivativeValue = floatingPointConvertor.bytesToFloat(new byte[]{sourceBytes.get(offset + 5), sourceBytes.get(offset + 4)});
            incomingDerivativeValueDelta = floatingPointConvertor.bytesToFloat(new byte[]{sourceBytes.get(offset + 7), sourceBytes.get(offset + 6)});
            outgoingDerivativeValue = floatingPointConvertor.bytesToFloat(new byte[]{sourceBytes.get(offset + 9), sourceBytes.get(offset + 8)});
            outgoingDerivativeValueDelta = floatingPointConvertor.bytesToFloat(new byte[]{sourceBytes.get(offset + 11), sourceBytes.get(offset + 10)});
            currentOffset += 12;

            for (int i = 0; i < entriesCount; i++) {
                entries.add(buildEntry(sourceBytes.subList(currentOffset, currentOffset + 4), true));
                currentOffset+=4;
            }
            return new Value6(value, valueDelta, incomingDerivativeValue, incomingDerivativeValueDelta, outgoingDerivativeValue, outgoingDerivativeValueDelta, offset, entries);
        }
    }

    private Value5.Entry buildEntry(List<Byte> sourceBytes, boolean halfPrecession){
        int absoluteFrameIndex;
        int valueQuantum;
        int incomingDerivativeQuantum;
        int outgoingDerivativeQuantum;

        if (!halfPrecession) {
            absoluteFrameIndex = Short.toUnsignedInt(ByteBuffer.wrap(new byte[]{sourceBytes.get(1), sourceBytes.get(0)}).getShort());
            valueQuantum = Short.toUnsignedInt(ByteBuffer.wrap(new byte[]{sourceBytes.get(3), sourceBytes.get(2)}).getShort());
            incomingDerivativeQuantum = Short.toUnsignedInt(ByteBuffer.wrap(new byte[]{sourceBytes.get(5), sourceBytes.get(4)}).getShort());
            outgoingDerivativeQuantum = Short.toUnsignedInt(ByteBuffer.wrap(new byte[]{sourceBytes.get(7), sourceBytes.get(6)}).getShort());
        } else {
            absoluteFrameIndex = Byte.toUnsignedInt(sourceBytes.get(0));
            valueQuantum = Byte.toUnsignedInt(sourceBytes.get(1));
            incomingDerivativeQuantum = Byte.toUnsignedInt(sourceBytes.get(2));
            outgoingDerivativeQuantum = Byte.toUnsignedInt(sourceBytes.get(3));
        }
        return new Value5.Entry(absoluteFrameIndex, valueQuantum, incomingDerivativeQuantum, outgoingDerivativeQuantum);
    }
}
