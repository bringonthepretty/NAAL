package com.wah.naal.model.motionfile.value.impl;

import java.util.List;

/**
 * This class represents value type 6. header size 12 bytes
 */
public class Value6 extends Value5 {

    /**
     * @param value                        p offset 0x00, size 2 bytes, c type pghalf
     * @param valueDelta                   dp offset 0x02, size 2 bytes, c type pghalf
     * @param incomingDerivativeValue      m0 offset 0x04, size 2 bytes, c type pghalf
     * @param incomingDerivativeValueDelta dm0 offset 0x06, size 2 bytes, c type pghalf
     * @param outgoingDerivativeValue      m1 offset 0x08, size 2 bytes, c type pghalf
     * @param outgoingDerivativeValueDelta dm1 offset 0x0A, size 2 bytes, c type pghalf
     * @param offset                       offset of value from file start
     * @param entries                      list of entries. see description of {@link Entry}
     */
    public Value6(Float value, Float valueDelta, Float incomingDerivativeValue, Float incomingDerivativeValueDelta,
                  Float outgoingDerivativeValue, Float outgoingDerivativeValueDelta, Integer offset,
                  List<Entry> entries) {
        super(value, valueDelta, incomingDerivativeValue, incomingDerivativeValueDelta,
                outgoingDerivativeValue, outgoingDerivativeValueDelta, offset, entries);
    }

    public Value6() {

    }

    @Override
    public int getType() {
        return 6;
    }

    @Override
    public String toString() {
        return "Value6{" +
                "offset=" + offset +
                ", value=" + value +
                ", valueDelta=" + valueDelta +
                ", incomingDerivativeValue=" + incomingDerivativeValue +
                ", incomingDerivativeValueDelta=" + incomingDerivativeValueDelta +
                ", outgoingDerivativeValue=" + outgoingDerivativeValue +
                ", outgoingDerivativeValueDelta=" + outgoingDerivativeValueDelta +
                ", entries=" + entries +
                '}';
    }
}
