package io.manbang.ebatis.sample.condition.base;

import com.google.common.collect.Lists;
import io.manbang.ebatis.core.provider.BoostingProvider;

/**
 * @author weilong.hu
 * @since 2021/4/19 18:56
 */
public class Load implements BoostingProvider {
    @Override
    public Object positive() {
        Protocol protocol = new Protocol();
        protocol.setProtocolStatus(0);
        RateMode rateMode = new RateMode();
        rateMode.setRateModeFlag(0);
        protocol.setRateMode(rateMode);
        return protocol;
    }

    @Override
    public Object negative() {
        return SecurityTran.builder().securityTran(Lists.newArrayList(7, 8, 9)).build();
    }
}
