package com.ymm.ebatis.sample.condition;

import com.google.common.collect.Lists;
import com.ymm.ebatis.core.annotation.Field;
import com.ymm.ebatis.core.annotation.Must;
import com.ymm.ebatis.core.annotation.Should;
import com.ymm.ebatis.core.domain.Range;
import com.ymm.ebatis.core.domain.ScoreFunction;
import com.ymm.ebatis.core.domain.ScoreFunctionMode;
import com.ymm.ebatis.core.domain.Script;
import com.ymm.ebatis.core.provider.ScoreFunctionProvider;
import lombok.Builder;
import lombok.Data;
import org.elasticsearch.common.lucene.search.function.FieldValueFactorFunction;

import java.util.List;

/**
 * @author weilong.hu
 * @since 2020/6/15 17:21
 */
@Data
public class RecentOrderCondition extends SampleRecentOrderCondition implements ScoreFunctionProvider {

    @Must
    private Integer cargoType = 2;
    /**
     * 基本类型集合
     */
    @Must
    private List<Integer> orderSource = Lists.newArrayList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
    /**
     * 基本类型集合
     */
    @Must
    @Field("orderType")
    private Integer[] type = {0, 1, 2, 3, 4, 5, 6, 7, 8};
    /**
     * 嵌套条件
     */
    @Must
    private Protocol condition = new Protocol();
    /**
     * 非基本类型集合
     */
    @Must
    private List<SecurityTran> securityTranList = Lists.newArrayList(
            SecurityTran.builder().securityTran(Lists.newArrayList(1, 2, 3)).build(),
            SecurityTran.builder().securityTran(Lists.newArrayList(4, 5, 6)).build());

    /**
     * 非基本类型集合
     */
    @Must
    private SecurityTran[] securityTrans = {
            SecurityTran.builder().securityTran(Lists.newArrayList(7, 8, 9)).build(),
            SecurityTran.builder().securityTran(Lists.newArrayList(10, 11, 12)).build()};

    @Must
    private Range<Integer> channel = Range.of(1, 100).closeLeft();

    @Must
    private Script script = Script.stored("666");

    //todo 是否支持 集合Range Script
    @Should
    private List<Range<Integer>> channels = Lists.newArrayList(Range.of(100, 200).closeLeft(), Range.of(300, 500).closeLeft());
    //todo 是否支持 数组Range Script
    @Should(minimumShouldMatch = "2")
    private Script[] scripts = new Script[]{Script.stored("888"), Script.stored("1024")};
    /**
     * 动态化计算实例类型
     */
    @Must
    private Object shipperInfo = ShipperInfo.builder().
            shipperTelephone(18704040001L).
            shipperTelephoneMask(999187242798320001L).
            shipperUserId(289912911L).
            build();

    @Must
    private Object[] shipperInfos = {ShipperInfo.builder().
            shipperTelephone(18036666725L).
            shipperTelephoneMask(99918036666L).
            shipperUserId(66642L).
            build()};

    @Override
    public ScoreFunction getFunction() {
        return ScoreFunction.fieldValueFactor("startCityId", 10, 10, FieldValueFactorFunction.Modifier.LN);
    }

    @Override
    public ScoreFunctionMode getFunctionMode() {
        return null;
    }


    @Data
    public static class Protocol {

        @Must
        private Integer protocolStatus = 0;
        @Must
        private RateMode rateMode = new RateMode();

    }

    @Data
    public static class RateMode {
        private Integer rateModeFlag = 0;

    }

    @Data
    @Builder
    public static class SecurityTran {
        private List<Integer> securityTran;
    }

    @Data
    @Builder
    public static class ShipperInfo {
        private Long shipperUserId;
        private Long shipperTelephone;
        private Long shipperTelephoneMask;
    }
}
