package io.manbang.ebatis.sample.condition.base;

import io.manbang.ebatis.core.provider.MultiMatchFieldProvider;
import lombok.Data;

/**
 * @author weilong.hu
 * @since 2021/5/24 10:38
 */
@Data
public class Cargo implements MultiMatchFieldProvider {

    @Override
    public String[] getFields() {
        return new String[]{"title^1.2", "address"};
    }

    @Override
    public Object text() {
        return "semitrailer";
    }

}
