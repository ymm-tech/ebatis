package com.ymm.ebatis.core.request;

import com.ymm.ebatis.core.annotation.UpdateByQuery;
import com.ymm.ebatis.core.common.ActiveShardCountUtils;
import com.ymm.ebatis.core.meta.MethodMeta;
import com.ymm.ebatis.core.provider.ScriptProvider;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;
import org.elasticsearch.search.builder.SearchSourceBuilder;

/**
 * @author duoliang.zhang
 */
class UpdateByQueryRequestFactory extends AbstractRequestFactory<UpdateByQuery, UpdateByQueryRequest> {
    static final UpdateByQueryRequestFactory INSTANCE = new UpdateByQueryRequestFactory();

    private UpdateByQueryRequestFactory() {
    }

    @Override
    protected void setAnnotationMeta(UpdateByQueryRequest request, UpdateByQuery updateByQuery) {
        request.setSlices(updateByQuery.slices())
                .setRequestsPerSecond(updateByQuery.requestsPerSecond())
                .setRefresh(updateByQuery.refresh())
                .setTimeout(updateByQuery.timeout())
                .setMaxRetries(updateByQuery.maxRetries())
                .setWaitForActiveShards(ActiveShardCountUtils.getActiveShardCount(updateByQuery.waitForActiveShards()))
                .setShouldStoreResult(updateByQuery.shouldStoreResult())
                .setBatchSize(updateByQuery.batchSize())
                .setConflicts(updateByQuery.conflicts());

        SearchRequest searchRequest = request.getSearchRequest();
        searchRequest.preference(StringUtils.trimToNull(updateByQuery.preference()))
                .requestCache(updateByQuery.requestCache())
        ;

        int maxDocs = updateByQuery.maxDocs();
        if (maxDocs > 0) {
            request.setMaxDocs(maxDocs);
        }

        long keepAlive = updateByQuery.scrollKeepAlive();
        if (keepAlive > 0) {
            request.setScroll(TimeValue.timeValueMillis(keepAlive));
        }
    }

    @Override
    protected UpdateByQueryRequest doCreate(MethodMeta meta, Object[] args) {
        SearchRequest searchRequest = RequestFactory.search().create(meta, args);
        SearchSourceBuilder source = searchRequest.source();

        UpdateByQueryRequest request = new UpdateByQueryRequest();
        request.getSearchRequest().source(source);
        request.indices(meta.getIndices());
        Object condition = args[0];

        if (condition instanceof ScriptProvider) {
            request.setScript(((ScriptProvider) condition).getScript().toEsScript());
        }
        searchRequest.source(source);
        return request;
    }
}
