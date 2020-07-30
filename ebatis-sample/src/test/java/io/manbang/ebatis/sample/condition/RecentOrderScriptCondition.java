package io.manbang.ebatis.sample.condition;

import io.manbang.ebatis.core.domain.Script;
import io.manbang.ebatis.core.provider.ScriptProvider;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Collections;

/**
 * @author weilong.hu
 * @since 2020/7/2 17:10
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class RecentOrderScriptCondition extends SampleRecentOrderCondition implements ScriptProvider {
    public static final Script SCRIPT = Script.inline("ctx._source.shipperUserId += params.count", Collections.singletonMap("count", 1024));

    @Override
    public Script getScript() {
        return SCRIPT;
    }
}
