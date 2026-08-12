package io.manbang.ebatis.sample.condition.base;

import lombok.Builder;
import lombok.Data;

/**
 * @author weilong.hu
 * @since 2020/7/13 14:23
 */
@Data
@Builder
public class ShipperInfo {
    private Long shipperUserId;
    private Long shipperTelephone;
    private Long shipperTelephoneMask;
}
