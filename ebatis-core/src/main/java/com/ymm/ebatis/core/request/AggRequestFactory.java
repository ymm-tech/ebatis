package com.ymm.ebatis.core.request;

import com.ymm.ebatis.core.annotation.Agg;
import com.ymm.ebatis.core.domain.Aggregation;
import com.ymm.ebatis.core.meta.MethodMeta;
import com.ymm.ebatis.core.meta.ParameterMeta;
import com.ymm.ebatis.core.provider.AggProvider;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;

import java.util.Optional;

/**
 * 聚合请求工厂，不同的聚合请求会被交给具体的聚合请求工厂去创建
 *
 * @author 章多亮
 * @since 2020/1/3 11:31
 */
class AggRequestFactory extends AbstractRequestFactory<Agg, SearchRequest> {
    public static final AggRequestFactory INSTANCE = new AggRequestFactory();

    private AggRequestFactory() {
    }

    @Override
    protected void setAnnotationMeta(SearchRequest request, Agg agg) {
        request.preference(StringUtils.trimToNull(agg.preference()))
                .searchType(agg.searchType());

        if (agg.aggOnly()) {
            request.source().fetchSource(false).size(0);
        }
    }

    @Override
    protected SearchRequest doCreate(MethodMeta meta, Object[] args) {
        Optional<ParameterMeta> conditionMeta = meta.findConditionParameter();
        Object condition = conditionMeta.map(p -> p.getValue(args)).orElse(null);

        SearchRequest request;
        if (condition != null) {
            request = RequestFactory.search().create(meta, args);
        } else {
            request = new SearchRequest(ArrayUtils.EMPTY_STRING_ARRAY);
        }

        if (condition instanceof AggProvider) {
            AggProvider aggProvider = (AggProvider) condition;
            if (ArrayUtils.isNotEmpty(aggProvider.getAggregations())) {
                for (Aggregation agg : aggProvider.getAggregations()) {
                    request.source().aggregation(agg.toAggBuilder());
                }
            }
        }
        return request;
    }


}
