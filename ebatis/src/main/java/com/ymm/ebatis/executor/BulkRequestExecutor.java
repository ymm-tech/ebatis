package com.ymm.ebatis.executor;

import com.ymm.ebatis.annotation.Bulk;
import com.ymm.ebatis.annotation.BulkType;
import com.ymm.ebatis.cluster.Cluster;
import com.ymm.ebatis.meta.MethodMeta;
import com.ymm.ebatis.request.RequestFactory;
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
class BulkRequestExecutor extends AbstractRequestExecutor<BulkRequest> {
    static final BulkRequestExecutor INSTANCE = new BulkRequestExecutor();

    private BulkRequestExecutor() {
    }

    @Override
    protected TriFunction<Cluster, BulkRequest, ResponseExtractor<?>, Object> getExecutor(MethodMeta method) {
        return method.getResultType()::bulk;
    }


    @Override
    protected RequestFactory<BulkRequest> getRequestFactory() {
        return RequestFactory.bulk();
    }

    private void setIndexAndType(MethodMeta method, BulkRequest request) {
        Bulk bulk = method.getAnnotation(Bulk.class);
        BulkType type = bulk.bulkType();

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
