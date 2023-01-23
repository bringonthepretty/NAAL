package com.wah.naal.model.motionfile.value.builder.api;

import com.wah.naal.model.motionfile.value.api.Value;
import com.wah.naal.model.motionfile.value.builder.impl.Value0Builder;
import com.wah.naal.model.motionfile.value.builder.impl.Value2Builder;
import com.wah.naal.model.motionfile.value.builder.impl.Value5Builder;
import com.wah.naal.model.motionfile.value.impl.Value4;
import com.wah.naal.model.motionfile.value.impl.Value1;
import com.wah.naal.model.motionfile.value.impl.Value7;
import com.wah.naal.model.motionfile.value.impl.Value8;

import java.util.List;

/**
 * This class represents {@link Value}'s builder
 * @param <T> type of value
 */
public interface ValueBuilder<T extends Value> {

    T build(List<Byte> sourceBytes, int offset, int entriesCount, boolean halfPrecision);

    static Value build(List<Byte> sourceBytes, int recordOffset, int unionValue, int entriesCount, int recordType){
        return switch (recordType) {
            case 0 -> new Value0Builder().build(sourceBytes, recordOffset + 8, entriesCount, false);
            case 1 -> new Value1();
            case 2 -> new Value2Builder().build(sourceBytes, unionValue + recordOffset, entriesCount, false);
            case 3 -> new Value2Builder().build(sourceBytes, unionValue + recordOffset, entriesCount,  true);
            case 4 -> new Value4();
            case 5 -> new Value5Builder().build(sourceBytes, unionValue + recordOffset, entriesCount, false);
            case 6 -> new Value5Builder().build(sourceBytes, unionValue + recordOffset, entriesCount, true);
            case 7 -> new Value7();
            case 8 -> new Value8();
            default -> throw new IllegalArgumentException("Unknown record type " + recordType);
        };
    }
}
