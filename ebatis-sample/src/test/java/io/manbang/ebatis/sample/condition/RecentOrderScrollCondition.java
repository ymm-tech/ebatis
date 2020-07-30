package io.manbang.ebatis.sample.condition;

import io.manbang.ebatis.core.domain.Range;
import io.manbang.ebatis.core.provider.DefaultScrollProvider;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * @author weilong.hu
 * @since 2020/7/3 10:39
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class RecentOrderScrollCondition extends DefaultScrollProvider {
    private Range<String> type = Range.of("0", "20");
}
