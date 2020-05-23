package com.ymm.ebatis.core.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ymm.ebatis.core.annotation.Index;
import com.ymm.ebatis.core.common.DslUtils;
import com.ymm.ebatis.core.meta.MethodMeta;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.support.ActiveShardCount;
import org.elasticsearch.common.xcontent.XContentType;

/**
 * @author 章多亮
 * @since 2019/12/17 19:19
 */
@Slf4j
public class IndexRequestFactory extends AbstractRequestFactory<Index, IndexRequest> {
    public static final IndexRequestFactory INSTANCE = new IndexRequestFactory();

    private IndexRequestFactory() {
    }

    @Override
    protected void setOptionalMeta(IndexRequest request, Index index) {
        ActiveShardCount activeShardCount = DslUtils.getActiveShardCount(index.waitForActiveShards());

        request.setRefreshPolicy(index.refreshPolicy())
                .versionType(index.versionType())
                .waitForActiveShards(activeShardCount)
                .timeout(index.timeout())
                .routing(index.routing())
                .opType(index.opType());

        if (StringUtils.isNotBlank(index.id())) {
            request.id(String.valueOf(request.sourceAsMap().get(index.id())));
        }

        if (StringUtils.isNotBlank(index.pipeline())) {
            request.setPipeline(index.pipeline());
        }
    }

    @Override
    protected IndexRequest doCreate(MethodMeta meta, Object[] args) {
        IndexRequest request = new IndexRequest();

        ObjectMapper mapper = new ObjectMapper();
        Object doc = args[0];
        byte[] source;
        try {
            source = mapper.writeValueAsBytes(doc);
        } catch (JsonProcessingException e) {
            log.error("条件转换成JSON字节数组异常：{}", doc, e);
            source = new byte[0];
        }
        request.source(source, XContentType.JSON);

        setIdAndVersion(doc, request::id, request::version);

        return request;
    }
}
