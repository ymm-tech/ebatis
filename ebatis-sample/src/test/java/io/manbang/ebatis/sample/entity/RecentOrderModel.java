package io.manbang.ebatis.sample.entity;

import io.manbang.ebatis.core.provider.IdProvider;
import lombok.Data;

/**
 * @author weilong.hu
 * @since 2020/6/28 18:43
 */
@Data
public class RecentOrderModel implements IdProvider {
    private Long cargoId = 10124512292666L;
    private String driverUserName = "老铁666啊";
    private String loadAddress = "我滴老家就住在这个屯";
    private Integer shipperUserId = 0;

    @Override
    public String getId() {
        return String.valueOf(cargoId);
    }
}
