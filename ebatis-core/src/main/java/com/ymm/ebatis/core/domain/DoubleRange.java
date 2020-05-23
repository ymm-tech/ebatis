package com.ymm.ebatis.core.domain;

class DoubleRange extends NumberRange<Double> {
    public DoubleRange(Double value) {
        super(value);
    }

    public DoubleRange(Double min, Double max) {
        super(min, max);
    }

    @Override
    protected void doExpand(Double leftDelta, Double rightDelta) {
        setMax(getValue() - rightDelta).setMin(getValue() - leftDelta);
    }
}
