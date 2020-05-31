package com.ymm.ebatis.core.domain;

import java.util.Date;

class DateRange extends AbstractRange<Date, Long> {
    public DateRange(Date value) {
        super(value);
    }

    public DateRange(Date min, Date max) {
        super(min, max);
    }


    @Override
    protected void doExpand(Long leftDelta, Long rightDelta) {
        setMin(new Date(getValue().getTime() - leftDelta)).setMax(new Date(getValue().getTime() + rightDelta));
    }
}
