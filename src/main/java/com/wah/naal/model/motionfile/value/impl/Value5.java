package com.wah.naal.model.motionfile.value.impl;

import com.wah.naal.model.motionfile.value.api.Value;

import java.util.List;
import java.util.Objects;

/**
 * This class represents value type 5. header size 24 bytes
 */
public class Value5 implements Value {
    protected Float value;
    protected Float valueDelta;
    protected Float incomingDerivativeValue;
    protected Float incomingDerivativeValueDelta;
    protected Float outgoingDerivativeValue;
    protected Float outgoingDerivativeValueDelta;
    protected Integer offset;
    protected List<Entry> entries;

    /**
     * @param value                        p offset 0x00, size 4 bytes, c type float
     * @param valueDelta                   dp offset 0x04, size 4 bytes, c type float
     * @param incomingDerivativeValue      m0 offset 0x08, size 4 bytes, c type float
     * @param incomingDerivativeValueDelta dm0 offset 0x0C, size 4 bytes, c type float
     * @param outgoingDerivativeValue      m1 offset 0x10, size 4 bytes, c type float
     * @param outgoingDerivativeValueDelta dm1 offset 0x14, size 4 bytes, c type float
     * @param offset                       offset of value from file start
     * @param entries                      list of entries. see description of {@link Entry}
     */
    public Value5(Float value, Float valueDelta, Float incomingDerivativeValue, Float incomingDerivativeValueDelta,
                  Float outgoingDerivativeValue, Float outgoingDerivativeValueDelta, Integer offset,
                  List<Entry> entries) {
        this.value = value;
        this.valueDelta = valueDelta;
        this.incomingDerivativeValue = incomingDerivativeValue;
        this.incomingDerivativeValueDelta = incomingDerivativeValueDelta;
        this.outgoingDerivativeValue = outgoingDerivativeValue;
        this.outgoingDerivativeValueDelta = outgoingDerivativeValueDelta;
        this.offset = offset;
        this.entries = entries;
    }

    public Value5 (){

    }

    @Override
    public int getType() {
        return 5;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public Float getValueDelta() {
        return valueDelta;
    }

    public void setValueDelta(Float valueDelta) {
        this.valueDelta = valueDelta;
    }

    public Float getIncomingDerivativeValue() {
        return incomingDerivativeValue;
    }

    public void setIncomingDerivativeValue(Float incomingDerivativeValue) {
        this.incomingDerivativeValue = incomingDerivativeValue;
    }

    public Float getIncomingDerivativeValueDelta() {
        return incomingDerivativeValueDelta;
    }

    public void setIncomingDerivativeValueDelta(Float incomingDerivativeValueDelta) {
        this.incomingDerivativeValueDelta = incomingDerivativeValueDelta;
    }

    public Float getOutgoingDerivativeValue() {
        return outgoingDerivativeValue;
    }

    public void setOutgoingDerivativeValue(Float outgoingDerivativeValue) {
        this.outgoingDerivativeValue = outgoingDerivativeValue;
    }

    public Float getOutgoingDerivativeValueDelta() {
        return outgoingDerivativeValueDelta;
    }

    public void setOutgoingDerivativeValueDelta(Float outgoingDerivativeValueDelta) {
        this.outgoingDerivativeValueDelta = outgoingDerivativeValueDelta;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public List<Entry> getEntries() {
        return entries;
    }

    public void setEntries(List<Entry> entries) {
        this.entries = entries;
    }

    @Override
    public String toString() {
        return "Value5{" +
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

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Value5) obj;
        return Objects.equals(this.value, that.value) &&
                Objects.equals(this.valueDelta, that.valueDelta) &&
                Objects.equals(this.incomingDerivativeValue, that.incomingDerivativeValue) &&
                Objects.equals(this.incomingDerivativeValueDelta, that.incomingDerivativeValueDelta) &&
                Objects.equals(this.outgoingDerivativeValue, that.outgoingDerivativeValue) &&
                Objects.equals(this.outgoingDerivativeValueDelta, that.outgoingDerivativeValueDelta) &&
                Objects.equals(this.offset, that.offset) &&
                Objects.equals(this.entries, that.entries);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, valueDelta, incomingDerivativeValue, incomingDerivativeValueDelta, outgoingDerivativeValue, outgoingDerivativeValueDelta, offset, entries);
    }


    /**
     * This class represents entry type 5 or 6. size 8 bytes (type 5), 4 bytes for half precision (type 6)
     *
     * @param absoluteFrameIndex        offset 0x00, size 2 bytes, c type uint16_t      for single precision<p>
     *                                  offset 0x00, size 1 byte, c type uint8_t        for half precision
     * @param valueQuantum              cp, offset 0x02, size 2 bytes, c type uint16_t  for single precision<p>
     *                                  offset 0x01, size 1 byte, c type uint8_t    for half precision
     * @param incomingDerivativeQuantum cm0, offset 0x04, size 2 bytes, c type uint16_t for single precision<p>
     *                                  offset 0x02, size 1 byte1, c type uint8_t  for half precision
     * @param outgoingDerivativeQuantum cm1, offset 0x06, size 2 bytes, c type uint16_t for single precision<p>
     *                                  offset 0x03, size 1 byte, c type uint8_t   for half precision
     */
    public record Entry(Integer absoluteFrameIndex, Integer valueQuantum, Integer incomingDerivativeQuantum,
                        Integer outgoingDerivativeQuantum) {

        @Override
        public String toString() {
            return "Entry{" +
                    "absoluteFrameIndex=" + absoluteFrameIndex +
                    ", valueQuantum=" + valueQuantum +
                    ", incomingDerivativeQuantum=" + incomingDerivativeQuantum +
                    ", outgoingDerivativeQuantum=" + outgoingDerivativeQuantum +
                    '}';
        }
    }
}
