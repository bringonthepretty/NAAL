package com.wah.naal.model.motionfile.value.builder.impl;

import com.wah.naal.model.motionfile.value.impl.Value3;
import com.wah.naal.utils.FloatingPointConvertor;
import com.wah.naal.model.motionfile.value.builder.api.ValueBuilder;
import com.wah.naal.model.motionfile.value.impl.Value2;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents builder for {@link Value2} and {@link Value3}
 */
public class Value2Builder implements ValueBuilder<Value2> {
    private final FloatingPointConvertor floatingPointConvertor = FloatingPointConvertor.getInstance();

    /**
     * Builds {@link Value2} or {@link Value3} depending on precision from source bytes
     * @param sourceBytes source bytes
     * @param offset start of value's bytes
     * @param entriesCount count of value's entries
     * @param halfPrecision data precision
     * @return {@link Value2} or {@link Value3} object
     */
    @Override
    public Value2 build(List<Byte> sourceBytes, int offset, int entriesCount, boolean halfPrecision) {
        List<Integer> entries = new ArrayList<>(entriesCount);
        int currentOffset = offset;
        float value;
        float valueDelta;

        if (!halfPrecision) {
            value = ByteBuffer.wrap(new byte[]{sourceBytes.get(offset + 3), sourceBytes.get(offset + 2), sourceBytes.get(offset + 1), sourceBytes.get(offset + 0)}).getFloat();
            valueDelta = ByteBuffer.wrap(new byte[]{sourceBytes.get(offset + 7), sourceBytes.get(offset + 6), sourceBytes.get(offset + 5), sourceBytes.get(offset + 4)}).getFloat();
            currentOffset += 8;

            for (int i = 0; i < entriesCount; i++) {
                entries.add(Short.toUnsignedInt(ByteBuffer.wrap(new byte[]{sourceBytes.get(currentOffset + 1), sourceBytes.get(currentOffset + 0)}).getShort()));
                currentOffset+=2;
            }
            return new Value2(value, valueDelta, entries, offset);
        } else {
            value = floatingPointConvertor.bytesToFloat(new byte[]{sourceBytes.get(offset + 1), sourceBytes.get(offset + 0)});
            valueDelta = floatingPointConvertor.bytesToFloat(new byte[]{sourceBytes.get(offset + 3), sourceBytes.get(offset + 2)});
            currentOffset += 4;

            for (int i = 0; i < entriesCount; i++) {
                entries.add(Byte.toUnsignedInt(sourceBytes.get(currentOffset + 0)));
                currentOffset+=1;
            }
            return new Value3(value, valueDelta, entries, offset);
        }
    }
}
