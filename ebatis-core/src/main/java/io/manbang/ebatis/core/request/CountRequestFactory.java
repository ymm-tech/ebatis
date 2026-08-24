package io.manbang.ebatis.core.request;

import io.manbang.ebatis.core.annotation.Count;
import io.manbang.ebatis.core.meta.MethodMeta;
import io.manbang.ebatis.core.provider.RoutingProvider;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.core.CountRequest;

class CountRequestFactory extends AbstractRequestFactory<Count, CountRequest> {
    static final CountRequestFactory INSTANCE = new CountRequestFactory();

    private CountRequestFactory() {
    }

    @Override
    protected void setAnnotationMeta(CountRequest request, Count count) {
        request.preference(StringUtils.trimToNull(count.preference()));
        if (!Float.isNaN(count.minScore())) {
            request.minScore(count.minScore());
        }
        if (count.terminateAfter() > 0) {
            request.terminateAfter(count.terminateAfter());
        }
    }

    @Override
    protected CountRequest doCreate(MethodMeta meta, Object[] args) {
        SearchRequest search = RequestFactory.search().create(meta, args);
        CountRequest request = new CountRequest(meta.getIndices(meta, args), search.source().query());
        Object condition = meta.findConditionParameter().map(parameter -> parameter.getValue(args)).orElse(null);
        if (condition instanceof RoutingProvider) {
            request.routing(((RoutingProvider) condition).routing());
        }
        return request;
    }
}
