package io.manbang.ebatis.core.request;

import io.manbang.ebatis.core.annotation.Reindex;
import io.manbang.ebatis.core.meta.MethodMeta;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.index.VersionType;
import org.elasticsearch.index.reindex.ReindexRequest;

class ReindexRequestFactory extends AbstractRequestFactory<Reindex, ReindexActionRequest> {
    static final ReindexRequestFactory INSTANCE = new ReindexRequestFactory();

    private ReindexRequestFactory() {
    }

    @Override
    protected void setAnnotationMeta(ReindexActionRequest wrapper, Reindex annotation) {
        ReindexRequest request = wrapper.getRequest();
        request.setConflicts(annotation.conflicts());
        request.setSlices(annotation.slices());
        request.setSourceBatchSize(annotation.batchSize());
        request.setRequestsPerSecond(annotation.requestsPerSecond());
        request.setRefresh(annotation.refresh());
        request.setTimeout(annotation.timeout());
        request.setRequireAlias(annotation.requireAlias());
        if (annotation.maxDocs() >= 0) {
            request.setMaxDocs(annotation.maxDocs());
        }
        String pipeline = StringUtils.trimToNull(annotation.pipeline());
        if (pipeline != null) {
            request.setDestPipeline(pipeline);
        }
        request.setDestOpType(annotation.opType());
        request.setDestVersionType(VersionType.valueOf(annotation.versionType().name()));
    }

    @Override
    protected ReindexActionRequest doCreate(MethodMeta meta, Object[] args) {
        Reindex annotation = meta.getAnnotation(Reindex.class);
        SearchRequest search = RequestFactory.search().create(meta, args);
        ReindexRequest request = new ReindexRequest()
                .setSourceIndices(meta.getIndices(meta, args))
                .setSourceQuery(search.source().query())
                .setDestIndex(annotation.destination());
        request.setShouldStoreResult(!annotation.waitForCompletion());
        return new ReindexActionRequest(request, annotation.waitForCompletion());
    }
}
