package com.ymm.ebatis.executor;

import com.ymm.ebatis.annotation.Bulk;
import com.ymm.ebatis.cluster.Cluster;
import com.ymm.ebatis.meta.MethodMeta;
import com.ymm.ebatis.request.RequestFactoryType;
import com.ymm.ebatis.response.ResponseExtractor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.common.TriFunction;

/**
 * @author 章多亮
 * @since 2019/12/26 19:29
 */
@Slf4j
class BulkMethodExecutor extends AbstractMethodExecutor<BulkRequest> {
    static final BulkMethodExecutor INSTANCE = new BulkMethodExecutor();

    private BulkMethodExecutor() {
    }

    @Override
    protected TriFunction<Cluster, BulkRequest, ResponseExtractor<?>, Object> getExecutor(MethodMeta method) {
        return method.getResultType()::bulk;
    }

    @Override
    protected BulkRequest createActionRequest(MethodMeta method, Object[] args) {
        BulkRequest request = method.getRequestFactory().create(MethodMeta.of(method.getMethod()), args);
        setIndexAndType(method, request);
        return request;
    }

    private void setIndexAndType(MethodMeta method, BulkRequest request) {
        Bulk bulk = method.getAnnotationRequired(Bulk.class);
        RequestFactoryType type = bulk.bulkType().getType();

        switch (type) {
            case INDEX:
                request.requests().stream()
                        .map(IndexRequest.class::cast)
                        .forEach(req -> req.index(method.getIndex()));

                break;
            case DELETE:
                request.requests().stream()
                        .map(DeleteRequest.class::cast)
                        .forEach(req -> req.index(method.getIndex()));
                break;
            case UPDATE:
                request.requests().stream()
                        .map(UpdateRequest.class::cast)
                        .forEach(req -> req.index(method.getIndex()));
                break;
            default:
                // do nothing
                log.error("未知类型");
                break;
        }
    }
}
