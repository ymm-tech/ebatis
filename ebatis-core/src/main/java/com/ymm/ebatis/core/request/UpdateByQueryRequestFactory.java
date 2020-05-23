package com.ymm.ebatis.core.request;

import com.ymm.ebatis.core.annotation.UpdateByQuery;
import com.ymm.ebatis.core.common.DslUtils;
import com.ymm.ebatis.core.meta.MethodMeta;
import com.ymm.ebatis.core.provider.ScriptProvider;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;
import org.elasticsearch.search.builder.SearchSourceBuilder;

/**
 * @author duoliang.zhang
 */
public class UpdateByQueryRequestFactory extends AbstractRequestFactory<UpdateByQuery, UpdateByQueryRequest> {
    public static final UpdateByQueryRequestFactory INSTANCE = new UpdateByQueryRequestFactory();

    private UpdateByQueryRequestFactory() {
    }

    @Override
    protected void setOptionalMeta(UpdateByQueryRequest request, UpdateByQuery updateByQuery) {
        request.setSlices(updateByQuery.slices())
                .setRefresh(updateByQuery.refresh())
                .setTimeout(TimeValue.parseTimeValue(updateByQuery.timeout(), "查询更新超时"))
                .setMaxRetries(updateByQuery.maxRetries())
                .setWaitForActiveShards(DslUtils.getActiveShardCount(updateByQuery.waitForActiveShards()))
                .setShouldStoreResult(updateByQuery.shouldStoreResult())
                .setBatchSize(updateByQuery.batchSize())
                .setConflicts(updateByQuery.conflicts());

        int maxDocs = updateByQuery.maxDocs();
        if (maxDocs > 0) {
            request.setMaxDocs(maxDocs);
        }

        TimeValue keepAlive = DslUtils.getScrollKeepAlive(updateByQuery.scrollKeepAlive());
        if (keepAlive != null) {
            request.setScroll(keepAlive);
        }

        request.getSearchRequest().routing(DslUtils.getRouting(updateByQuery.routing()));
    }

    @Override
    protected UpdateByQueryRequest doCreate(MethodMeta meta, Object[] args) {
        SearchRequest searchRequest = SearchRequestFactory.INSTANCE.create(meta, args);
        SearchSourceBuilder source = searchRequest.source();
        UpdateByQueryRequest request = new UpdateByQueryRequest();
        request.getSearchRequest().source(source);
        Object condition = args[0];

        if (condition instanceof ScriptProvider) {
            request.setScript(((ScriptProvider) condition).getScript().toEsScript());
        }
        searchRequest.source(source);
        return request;
    }
}
