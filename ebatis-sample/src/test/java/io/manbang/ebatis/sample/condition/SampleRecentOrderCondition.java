package io.manbang.ebatis.sample.condition;

import io.manbang.ebatis.core.annotation.Must;
import lombok.Data;

/**
 * @author weilong.hu
 * @since 2020/6/23 13:44
 */
@Data
public class SampleRecentOrderCondition {
    @Must
    private Long cargoId;
}
