package com.ymm.ebatis.sample.condition.base;

import com.ymm.ebatis.core.annotation.Prefix;
import lombok.Builder;
import lombok.Data;

/**
 * @author weilong.hu
 * @since 2020/7/13 14:23
 */
@Data
@Builder
@Prefix("shipper")
public class ShipperInfo {
    private Long shipperUserId;
    private Long shipperTelephone;
    private Long shipperTelephoneMask;
}
