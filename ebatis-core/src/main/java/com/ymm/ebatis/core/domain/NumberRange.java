package com.ymm.ebatis.core.domain;

abstract class NumberRange<N extends Number> extends AbstractRange<N, N> {
    public NumberRange(N value) {
        super(value);
    }

    public NumberRange(N min, N max) {
        super(min, max);
    }
}
