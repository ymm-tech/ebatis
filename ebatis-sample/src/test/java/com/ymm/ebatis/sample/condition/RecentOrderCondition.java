package com.ymm.ebatis.sample.condition;

import com.ymm.ebatis.core.annotation.Field;
import com.ymm.ebatis.core.annotation.Must;
import com.ymm.ebatis.core.annotation.QueryType;
import com.ymm.ebatis.core.annotation.Should;
import com.ymm.ebatis.core.domain.Range;
import com.ymm.ebatis.core.domain.ScoreFunction;
import com.ymm.ebatis.core.domain.ScoreFunctionMode;
import com.ymm.ebatis.core.domain.Script;
import com.ymm.ebatis.core.provider.ScoreFunctionProvider;
import com.ymm.ebatis.sample.condition.base.Protocol;
import com.ymm.ebatis.sample.condition.base.SecurityTran;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.elasticsearch.common.lucene.search.function.FieldValueFactorFunction;

import java.util.List;

/**
 * @author weilong.hu
 * @since 2020/6/15 17:21
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class RecentOrderCondition extends SampleRecentOrderCondition implements ScoreFunctionProvider {

    /**
     * 基本类型
     */
    @Must
    private Integer cargoType;
    /**
     * 基本类型集合
     */
    @Must
    private List<Integer> orderSource;

    /**
     * 基本类型集合
     */
    @Must
    @Field("orderType")
    private Integer[] type;

    /**
     * 嵌套条件
     */
    @Must
    private Protocol protocol;

    /**
     * 非基本类型集合
     */
    @Must
    private List<SecurityTran> securityTranList;

    /**
     * 非基本类型集合
     */
    @Must
    private SecurityTran[] securityTrans;

    /**
     * 单范围查询
     */
    @Must
    private Range<Integer> channel;

    /**
     * 脚本查询
     */
    @Must
    private Script script;

    /**
     * 多范围组合查询
     */
    @Should
    private List<Range<Integer>> channels;
    /**
     * 多脚本组合查询
     */
    @Should(minimumShouldMatch = "2")
    private Script[] scripts;
    /**
     * 动态化计算实例类型
     */
    @Must
    private Object shipperInfo;

    @Must
    private Object[] shipperInfos;

    @Must(queryType = QueryType.EXISTS)
    private boolean startAreaCode;

    @Must(queryType = QueryType.WILDCARD)
    private String unloadAddress;

    @Override
    public ScoreFunction getFunction() {
        return ScoreFunction.fieldValueFactor("startCityId", 10, 10, FieldValueFactorFunction.Modifier.LN);
    }

    @Override
    public ScoreFunctionMode getFunctionMode() {
        return null;
    }


}
