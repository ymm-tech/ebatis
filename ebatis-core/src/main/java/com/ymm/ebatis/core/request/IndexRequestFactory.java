package com.ymm.ebatis.core.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ymm.ebatis.core.annotation.Index;
import com.ymm.ebatis.core.meta.MethodMeta;
import com.ymm.ebatis.core.provider.IdProvider;
import com.ymm.ebatis.core.provider.VersionProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.support.ActiveShardCount;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.xcontent.XContentType;

import static com.ymm.ebatis.core.common.ObjectMapperHolder.objectMapper;

/**
 * @author 章多亮
 * @since 2019/12/17 19:19
 */
@Slf4j
class IndexRequestFactory extends AbstractRequestFactory<Index, IndexRequest> {
    static final IndexRequestFactory INSTANCE = new IndexRequestFactory();

    private IndexRequestFactory() {
    }

    @Override
    protected void setAnnotationMeta(IndexRequest request, Index index) {
        request.setRefreshPolicy(index.refreshPolicy())
                .versionType(index.versionType())
                .waitForActiveShards(ActiveShardCount.parseString(index.waitForActiveShards()))
                .timeout(index.timeout())
                .opType(index.opType());

        if (StringUtils.isNotBlank(index.id())) {
            request.id(String.valueOf(request.sourceAsMap().get(index.id())));
        }

        request.setPipeline(StringUtils.trimToNull(index.pipeline()));
    }

    @Override
    protected IndexRequest doCreate(MethodMeta meta, Object[] args) {
        IndexRequest request = Requests.indexRequest(meta.getIndex());
        setTypeIfNecessary(meta, request::type);

        Object doc = meta.getConditionParameter().getValue(args);

        ObjectMapper mapper = objectMapper();
        byte[] source;
        try {
            source = mapper.writeValueAsBytes(doc);
        } catch (JsonProcessingException e) {
            log.error("条件转换成JSON字节数组异常：{}", doc, e);
            source = new byte[0];
        }

        request.source(source, XContentType.JSON);
        if (doc instanceof IdProvider) {
            request.id(((IdProvider) doc).getId());
        }

        if (doc instanceof VersionProvider) {
            request.version(((VersionProvider) doc).getVersion());
        }

        return request;
    }
}
