package com.wah.naal.model.motionfile.value.impl;

import com.wah.naal.model.motionfile.value.api.Value;

public class Value1 implements Value {
    private Float value;

    public Value1(){
        throw new UnsupportedOperationException();
    }

    @Override
    public int getType() {
        return 1;
    }
}
