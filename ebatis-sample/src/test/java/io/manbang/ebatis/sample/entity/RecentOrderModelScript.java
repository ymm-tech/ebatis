package io.manbang.ebatis.sample.entity;

import io.manbang.ebatis.core.domain.Script;
import io.manbang.ebatis.core.provider.IdProvider;
import io.manbang.ebatis.core.provider.ScriptProvider;
import lombok.Data;

import java.util.Collections;

/**
 * @author weilong.hu
 * @since 2020/6/30 18:21
 */
@Data
public class RecentOrderModelScript implements IdProvider, ScriptProvider {
    public static final Script SCRIPT = Script.inline("ctx._source.shipperUserId += params.count", Collections.singletonMap("count", 1024));
    private Long cargoId = 10124512292666L;

    @Override
    public String getId() {
        return String.valueOf(cargoId);
    }

    @Override
    public Script getScript() {
        return SCRIPT;
    }
}
