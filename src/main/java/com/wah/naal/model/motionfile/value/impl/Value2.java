package com.wah.naal.model.motionfile.value.impl;

import com.wah.naal.model.motionfile.value.api.Value;

import java.util.List;
import java.util.Objects;

/**
 * This class represents value type 2. header size 8 bytes
 */
public class Value2 implements Value {
    protected Float value;
    protected Float valueDelta;
    protected List<Integer> entries;
    protected Integer offset;

    /**
     * @param value      p offset 0x00, size 4 bytes, c type float
     * @param valueDelta dp offset 0x04, size 4 bytes, c type float
     * @param entries    cp offset from value start 0x08, size 2 bytes times entries count.
     *                   entry contains one value quantum (cp), c type uint16_t, size 2 bytes, offset from entry start 0x00 for single precision <p>
     * @param offset     offset of value from file start
     */
    public Value2(Float value, Float valueDelta, List<Integer> entries, Integer offset) {
        this.value = value;
        this.valueDelta = valueDelta;
        this.entries = entries;
        this.offset = offset;
    }

    public Value2(){

    }

    @Override
    public int getType() {
        return 2;
    }

    @Override
    public String toString() {
        return "Value2{" +
                "offset=" + offset +
                ", value=" + value +
                ", valueDelta=" + valueDelta +
                ", entries=" + entries +
                '}';
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

    public List<Integer> getEntries() {
        return entries;
    }

    public void setEntries(List<Integer> entries) {
        this.entries = entries;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Value2) obj;
        return Objects.equals(this.value, that.value) &&
                Objects.equals(this.valueDelta, that.valueDelta) &&
                Objects.equals(this.entries, that.entries) &&
                Objects.equals(this.offset, that.offset);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, valueDelta, entries, offset);
    }

}
