package io.manbang.ebatis.sample.condition.base;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author weilong.hu
 * @since 2020/7/13 14:23
 */
@Data
@Builder
public class SecurityTran {
    private List<Integer> securityTran;
}
