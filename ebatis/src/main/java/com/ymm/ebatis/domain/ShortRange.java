package com.ymm.ebatis.domain;

class ShortRange extends NumberRange<Short> {
    public ShortRange(Short value) {
        super(value);
    }

    public ShortRange(Short min, Short max) {
        super(min, max);
    }

    @Override
    protected void doExpand(Short leftDelta, Short rightDelta) {
        setMin((short) (getValue() - leftDelta)).setMax((short) (getValue() - rightDelta));
    }
}
