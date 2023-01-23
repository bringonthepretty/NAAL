package com.wah.naal.model.motionfile.value.impl;

import java.util.List;

/**
 * This class represents value type 3. header size 4 bytes
 */
public class Value3 extends Value2 {

    /**
     * @param value      p offset 0x00, size 2 bytes, c type pghalf
     * @param valueDelta dp offset 0x02, size 2 bytes, c type pghalf
     * @param entries    cp offset from value start 0x04, size 1 byte times entries count
     *                   entry contains one value quantum (cp), c type uint8_t, size 1 byte
     * @param offset     offset of value from file start
     */
    public Value3(Float value, Float valueDelta, List<Integer> entries, Integer offset) {
        super (value, valueDelta, entries, offset);
    }

    public Value3() {

    }

    @Override
    public int getType() {
        return 3;
    }

    @Override
    public String toString() {
        return "Value3{" +
                "offset=" + super.offset +
                ", value=" + super.value +
                ", valueDelta=" + super.valueDelta +
                ", entries=" + super.entries +
                '}';
    }
}
