package io.manbang.ebatis.sample.condition.base;

import io.manbang.ebatis.core.annotation.Must;
import io.manbang.ebatis.core.annotation.QueryType;
import io.manbang.ebatis.core.provider.DisMaxProvider;
import lombok.Data;

/**
 * @author weilong.hu
 * @since 2021/5/24 10:27
 */
@Data
public class Truck implements DisMaxProvider {

    @Override
    public Object[] conditions() {
        final TruckCondition truck1 = new TruckCondition();
        truck1.setTitle("high quality cargo");
        final TruckCondition truck2 = new TruckCondition();
        truck2.setBody("high quality cargo");
        return new Object[]{truck1, truck2};
    }

    @Data
    public static class TruckCondition {
        @Must(queryType = QueryType.MATCH)
        private String title;
        @Must(queryType = QueryType.MATCH)
        private String body;
    }
}
