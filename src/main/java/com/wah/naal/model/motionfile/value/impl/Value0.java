package com.wah.naal.model.motionfile.value.impl;

import com.wah.naal.model.motionfile.value.api.Value;

import java.util.Objects;

/**
 * This class represents value type 0. size 4 bytes, c type float
 */
public class Value0 implements Value {
    private Float value;

    /**
     * @param value offset 0x00, size 4 bytes, c type float
     */
    public Value0(Float value) {
        this.value = value;
    }

    @Override
    public int getType() {
        return 0;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Value0{" +
                "value=" + value +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Value0) obj;
        return Objects.equals(this.value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

}
