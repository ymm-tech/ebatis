package com.ymm.ebatis.core.request;

import com.ymm.ebatis.core.annotation.Bulk;
import com.ymm.ebatis.core.common.DslUtils;
import com.ymm.ebatis.core.meta.MethodMeta;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;

import java.util.Arrays;
import java.util.Collection;

import static com.ymm.ebatis.core.common.DslUtils.getFirstElement;

/**
 * @author 章多亮
 * @since 2019/12/26 15:04
 */
@Slf4j
public class BulkRequestFactory extends AbstractRequestFactory<Bulk, BulkRequest> {
    public static final RequestFactory INSTANCE = new BulkRequestFactory();

    private BulkRequestFactory() {
    }

    @Override
    protected void setOptionalMeta(BulkRequest request, Bulk bulk) {
        request.setRefreshPolicy(bulk.refreshPolicy())
                .timeout(bulk.timeout())
                .waitForActiveShards(DslUtils.getActiveShardCount(bulk.waitForActiveShards()));
        RequestFactoryType type = bulk.bulkType().getType();

        switch (type) {
            case INDEX:
                getFirstElement(bulk.index()).ifPresent(index -> request.requests().stream()
                        .map(IndexRequest.class::cast)
                        .forEach(req -> IndexRequestFactory.INSTANCE.setOptionalMeta(req, index)));

                break;
            case DELETE:
                getFirstElement(bulk.delete()).ifPresent(delete -> request.requests().stream()
                        .map(DeleteRequest.class::cast)
                        .forEach(req -> DeleteRequestFactory.INSTANCE.setOptionalMeta(req, delete)));

                break;
            case UPDATE:
                getFirstElement(bulk.update()).ifPresent(update -> request.requests().stream()
                        .map(UpdateRequest.class::cast)
                        .forEach(req -> UpdateRequestFactory.INSTANCE.setOptionalMeta(req, update)));
                break;
            default:
                // do nothing
                log.error("未知类型");
                break;
        }
    }

    @Override
    protected BulkRequest doCreate(MethodMeta meta, Object[] args) {
        Bulk bulk = meta.getAnnotationRequired(Bulk.class);

        Collection<?> documents = getAllDocuments(meta, args);

        DocWriteRequest<?>[] requests = buildRequests(meta, bulk, documents);
        BulkRequest request = new BulkRequest();
        request.add(requests);
        return request;
    }

    private DocWriteRequest<?>[] buildRequests(MethodMeta meta, Bulk bulk, Collection<?> documents) {
        RequestFactory builder = bulk.bulkType().getBuilder();
        return documents.stream()
                .map(d -> builder.create(meta, d))
                .map(DocWriteRequest.class::cast)
                .toArray(DocWriteRequest[]::new);
    }

    private Collection<?> getAllDocuments(MethodMeta meta, Object[] args) {
        Class<?> clazz = meta.getFirstParameter().getType();
        Collection<?> docs;

        if (Collection.class.isAssignableFrom(clazz)) {
            docs = ((Collection<?>) args[0]);
        } else if (clazz.isArray()) {
            docs = Arrays.asList((Object[]) args[0]);
        } else {
            throw new UnsupportedOperationException("入参必须是数据或者集合类型" + meta);
        }
        return docs;
    }
}
