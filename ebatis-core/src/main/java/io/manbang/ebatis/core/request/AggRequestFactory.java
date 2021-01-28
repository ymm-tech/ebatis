package io.manbang.ebatis.core.request;

import io.manbang.ebatis.core.annotation.Agg;
import io.manbang.ebatis.core.domain.Aggregation;
import io.manbang.ebatis.core.meta.MethodMeta;
import io.manbang.ebatis.core.meta.ParameterMeta;
import io.manbang.ebatis.core.provider.AggProvider;
import io.manbang.ebatis.core.provider.RoutingProvider;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;

import java.util.Optional;

/**
 * 聚合请求工厂
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
        if (condition instanceof RoutingProvider) {
            request.routing(((RoutingProvider) condition).routing());
        }
        return request;
    }


}
