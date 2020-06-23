package com.ymm.ebatis.sample.entity;

import com.ymm.ebatis.core.annotation.Field;
import com.ymm.ebatis.core.annotation.Ignore;
import lombok.Data;

/**
 * @author weilong.hu
 * @since 2020/6/15 17:23
 */
@Data
public class RecentOrder {
    //作为includes字段
    private Long cargoId;
    //不作为includes字段
    private static String cargoType;
    //不作为includes字段
    private volatile String tradeType;
    //不作为includes字段
    @Ignore
    private volatile String orderSource;
    //将end_province_code作为includes字段
    @Field("end_province_code")
    private String endProvinceCode;
}
