package com.ymm.ebatis.core.domain;

class IntegerRange extends NumberRange<Integer> {
    public IntegerRange(Integer value) {
        super(value);
    }

    public IntegerRange(Integer min, Integer max) {
        super(min, max);
    }

    @Override
    protected void doExpand(Integer leftDelta, Integer rightDelta) {
        setMin(getValue() - leftDelta).setMax(getValue() + rightDelta);

    }
}
