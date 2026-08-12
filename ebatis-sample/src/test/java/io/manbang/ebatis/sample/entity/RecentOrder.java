package io.manbang.ebatis.sample.entity;

import io.manbang.ebatis.core.annotation.Field;
import io.manbang.ebatis.core.annotation.Ignore;
import io.manbang.ebatis.core.domain.HighlightSource;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author weilong.hu
 * @since 2020/6/15 17:23
 */
@Data
public class RecentOrder implements HighlightSource {
    //作为includes字段
    private Long cargoId;
    //不作为includes字段
    private static String cargoType;
    //不作为includes字段
    private transient String tradeType;
    //不作为includes字段
    @Ignore
    private volatile String orderSource;
    //将end_province_code作为includes字段
    @Field("end_province_code")
    private String endProvinceCode;

    private Long companyId;
    private String driverUserName;
    private String loadAddress;
    private Map<String, List<String>> highlightSource;

    @Override
    public void setHighlightSource(Map<String, List<String>> highlightSource) {
        this.highlightSource = highlightSource;
    }
}
