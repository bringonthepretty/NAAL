package com.wah.naal.model.motionfile.value.builder.impl;

import com.wah.naal.model.motionfile.value.builder.api.ValueBuilder;
import com.wah.naal.model.motionfile.value.impl.Value0;

import java.nio.ByteBuffer;
import java.util.List;

/**
 * This class represents builder for {@link Value0}
 */
public class Value0Builder implements ValueBuilder<Value0> {

    /**
     * Builds {@link Value0} from source bytes
     * @param sourceBytes source bytes
     * @param offset start of value's bytes
     * @param entriesCount ignored
     * @param halfPrecision ignored
     * @return {@link Value0} object
     */
    @Override
    public Value0 build(List<Byte> sourceBytes, int offset, int entriesCount, boolean halfPrecision) {
        return new Value0(ByteBuffer.wrap(
                new byte[]{sourceBytes.get(offset + 3),
                        sourceBytes.get(offset + 2),
                        sourceBytes.get(offset + 1),
                        sourceBytes.get(offset + 0)})
                .getFloat());
    }
}
