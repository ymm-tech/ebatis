package io.manbang.ebatis.core.request;

import io.manbang.ebatis.core.annotation.DeleteByQuery;
import io.manbang.ebatis.core.common.ActiveShardCountUtils;
import io.manbang.ebatis.core.meta.MethodMeta;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.search.builder.SearchSourceBuilder;

/**
 * @author duoliang.zhang
 */
class DeleteByQueryRequestFactory extends AbstractRequestFactory<DeleteByQuery, DeleteByQueryRequest> {
    static final DeleteByQueryRequestFactory INSTANCE = new DeleteByQueryRequestFactory();

    private DeleteByQueryRequestFactory() {
    }

    @Override
    protected void setAnnotationMeta(DeleteByQueryRequest request, DeleteByQuery deleteByQuery) {
        request.setSlices(deleteByQuery.slices())
                .setRefresh(deleteByQuery.refresh())
                .setTimeout(deleteByQuery.timeout())
                .setMaxRetries(deleteByQuery.maxRetries())
                .setWaitForActiveShards(ActiveShardCountUtils.getActiveShardCount(deleteByQuery.waitForActiveShards()))
                .setShouldStoreResult(deleteByQuery.shouldStoreResult())
                .setBatchSize(deleteByQuery.batchSize())
                .setConflicts(deleteByQuery.conflicts());

        int maxDocs = deleteByQuery.maxDocs();
        if (maxDocs > 0) {
            request.setMaxDocs(maxDocs);
        }

        long keepAlive = deleteByQuery.scrollKeepAlive();
        if (keepAlive > 0) {
            request.setScroll(TimeValue.timeValueMillis(keepAlive));
        }
    }

    @Override
    protected DeleteByQueryRequest doCreate(MethodMeta meta, Object[] args) {
        SearchRequest searchRequest = RequestFactory.search().create(meta, args);
        SearchSourceBuilder source = searchRequest.source();

        DeleteByQueryRequest request = new DeleteByQueryRequest(meta.getIndices(meta, args));
        request.getSearchRequest().source(source);
        request.setRouting(searchRequest.routing());
        searchRequest.source(source);
        return request;
    }
}
