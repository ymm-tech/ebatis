package com.ymm.ebatis.domain;

class FloatRange extends NumberRange<Float> {
    public FloatRange(Float value) {
        super(value);
    }

    public FloatRange(Float min, Float max) {
        super(min, max);
    }

    @Override
    protected void doExpand(Float leftDelta, Float rightDelta) {
        setMax(getValue() - rightDelta).setMin(getValue() - leftDelta);
    }
}
