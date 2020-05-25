package com.ymm.ebatis.domain;

class LongRange extends NumberRange<Long> {
    public LongRange(Long value) {
        super(value);
    }

    public LongRange(Long min, Long max) {
        super(min, max);
    }


    @Override
    protected void doExpand(Long leftDelta, Long rightDelta) {
        setMin(getValue() - leftDelta).setMax(getValue() + rightDelta);
    }
}
