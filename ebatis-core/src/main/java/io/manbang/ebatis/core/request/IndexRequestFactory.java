package io.manbang.ebatis.core.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.manbang.ebatis.core.annotation.Index;
import io.manbang.ebatis.core.common.ActiveShardCountUtils;
import io.manbang.ebatis.core.common.ObjectMapperHolder;
import io.manbang.ebatis.core.exception.EbatisException;
import io.manbang.ebatis.core.meta.MethodMeta;
import io.manbang.ebatis.core.provider.IdProvider;
import io.manbang.ebatis.core.provider.ParentTaskProvider;
import io.manbang.ebatis.core.provider.ReplicaVersionProvider;
import io.manbang.ebatis.core.provider.RoutingProvider;
import io.manbang.ebatis.core.provider.VersionProvider;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.VersionType;

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
        val versionType = VersionType.valueOf(index.versionType().name());
        if (request.version() >= 0 && versionType == VersionType.INTERNAL) {
            throw new IllegalArgumentException("提供了版本号，版本类型，就不能是内部版本类型： VersionType.INTERNAL，请设置 Index#versionType = VersionType.EXTERNAL | VersionType.EXTERNAL_GTE");
        }

        request.setRefreshPolicy(WriteRequest.RefreshPolicy.valueOf(index.refreshPolicy().name()))
                .versionType(versionType)
                .waitForActiveShards(ActiveShardCountUtils.getActiveShardCount(index.waitForActiveShards()))
                .timeout(index.timeout())
                .opType(DocWriteRequest.OpType.valueOf(index.opType().name()))
                .setPipeline(StringUtils.trimToNull(index.finalPipeline()))
                .setFinalPipeline(StringUtils.trimToNull(index.finalPipeline()));
    }

    @Override
    protected IndexRequest doCreate(MethodMeta meta, Object[] args) {
        val request = Requests.indexRequest(meta.getIndex(meta, args));
        val doc = meta.getConditionParameter().getValue(args);
        val source = getSource(doc);

        request.source(source, XContentType.JSON);
        if (doc instanceof IdProvider) {
            request.id(((IdProvider) doc).id());
        }

        if (doc instanceof VersionProvider) {
            // 版本类型，有 Index#versionType 设置
            request.version(((VersionProvider) doc).version());
        }

        if (doc instanceof RoutingProvider) {
            request.routing(((RoutingProvider) doc).routing());
        }

        if (doc instanceof ReplicaVersionProvider) {
            val provider = (ReplicaVersionProvider) doc;
            request.setIfSeqNo(provider.seqNo());
            request.setIfPrimaryTerm(provider.primaryTerm());
        }

        if (doc instanceof ParentTaskProvider) {
            val provider = (ParentTaskProvider) doc;
            request.setParentTask(provider.nodeId(), provider.taskId());
        }

        return request;
    }

    private byte[] getSource(Object doc) {
        try {
            return ObjectMapperHolder.objectMapper().writeValueAsBytes(doc);
        } catch (JsonProcessingException e) {
            log.error("条件转换成JSON字节数组异常：{}", doc, e);
            throw new EbatisException("序列化文档异常", e);
        }
    }
}
