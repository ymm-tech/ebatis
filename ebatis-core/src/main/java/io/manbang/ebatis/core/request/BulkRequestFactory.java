package io.manbang.ebatis.core.request;

import io.manbang.ebatis.core.annotation.Bulk;
import io.manbang.ebatis.core.annotation.BulkType;
import io.manbang.ebatis.core.common.ActiveShardCountUtils;
import io.manbang.ebatis.core.meta.MetaUtils;
import io.manbang.ebatis.core.meta.MethodMeta;
import io.manbang.ebatis.core.meta.ParameterMeta;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.Requests;

import java.util.Arrays;
import java.util.Collection;


/**
 * @author 章多亮
 * @since 2019/12/26 15:04
 */
@Slf4j
public class BulkRequestFactory extends AbstractRequestFactory<Bulk, BulkRequest> {
    public static final RequestFactory<BulkRequest> INSTANCE = new BulkRequestFactory();

    private BulkRequestFactory() {
    }

    @Override
    protected void setAnnotationMeta(BulkRequest request, Bulk bulk) {
        request.setRefreshPolicy(bulk.refreshPolicy())
                .timeout(bulk.timeout())
                .waitForActiveShards(ActiveShardCountUtils.getActiveShardCount(bulk.waitForActiveShards()));
        BulkType type = bulk.bulkType();

        switch (type) {
            case INDEX:
                MetaUtils.findFirstElement(bulk.index()).ifPresent(index -> request.requests().stream()
                        .map(IndexRequest.class::cast)
                        .forEach(req -> IndexRequestFactory.INSTANCE.setAnnotationMeta(req, index)));

                break;
            case DELETE:
                MetaUtils.findFirstElement(bulk.delete()).ifPresent(delete -> request.requests().stream()
                        .map(DeleteRequest.class::cast)
                        .forEach(req -> DeleteRequestFactory.INSTANCE.setAnnotationMeta(req, delete)));

                break;
            case UPDATE:
                MetaUtils.findFirstElement(bulk.update()).ifPresent(update -> request.requests().stream()
                        .map(UpdateRequest.class::cast)
                        .forEach(req -> UpdateRequestFactory.INSTANCE.setAnnotationMeta(req, update)));
                break;
            default:
                // do nothing
                log.error("未知类型");
                break;
        }
    }

    @Override
    protected BulkRequest doCreate(MethodMeta meta, Object[] args) {
        Bulk bulk = meta.getAnnotation(Bulk.class);

        Collection<?> documents = getAllDocuments(meta, args);

        DocWriteRequest<?>[] requests = buildRequests(meta, bulk, documents);
        BulkRequest request = Requests.bulkRequest();
        request.add(requests);
        return request;
    }

    private DocWriteRequest<?>[] buildRequests(MethodMeta meta, Bulk bulk, Collection<?> documents) {
        RequestFactory<?> requestFactory = bulk.bulkType().getRequestFactory();
        return documents.stream()
                .map(d -> requestFactory.create(meta, d))
                .map(DocWriteRequest.class::cast)
                .toArray(DocWriteRequest[]::new);
    }

    private Collection<?> getAllDocuments(MethodMeta meta, Object[] args) {
        ParameterMeta parameterMeta = meta.getConditionParameter();

        Object arg = parameterMeta.getValue(args);

        Collection<?> docs;

        if (parameterMeta.isCollection()) {
            docs = (Collection<?>) arg;
        } else if (parameterMeta.isArray()) {
            docs = Arrays.asList((Object[]) arg);
        } else {
            throw new UnsupportedOperationException("入参必须是数据或者集合类型" + meta);
        }
        return docs;
    }
}
