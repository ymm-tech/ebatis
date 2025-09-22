package io.manbang.ebatis.core.request;

import io.manbang.ebatis.core.annotation.Delete;
import io.manbang.ebatis.core.common.ActiveShardCountUtils;
import io.manbang.ebatis.core.exception.ConditionNotSupportException;
import io.manbang.ebatis.core.meta.MethodMeta;
import io.manbang.ebatis.core.meta.ParameterMeta;
import io.manbang.ebatis.core.provider.IdProvider;
import io.manbang.ebatis.core.provider.ParentTaskProvider;
import io.manbang.ebatis.core.provider.ReplicaVersionProvider;
import io.manbang.ebatis.core.provider.RoutingProvider;
import io.manbang.ebatis.core.provider.VersionProvider;
import lombok.val;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.client.Requests;
import org.elasticsearch.index.VersionType;

/**
 * @author 章多亮
 * @since 2019/12/17 19:20
 */
class DeleteRequestFactory extends AbstractRequestFactory<Delete, DeleteRequest> {
    static final DeleteRequestFactory INSTANCE = new DeleteRequestFactory();

    private DeleteRequestFactory() {
    }

    @Override
    protected void setAnnotationMeta(DeleteRequest request, Delete delete) {
        val versionType = VersionType.valueOf(delete.versionType().name());
        val version = request.version();
        if (version >= 0 && versionType == VersionType.INTERNAL) {
            throw new IllegalArgumentException("提供了版本号，版本类型，就不能是内部版本类型： VersionType.INTERNAL，请设置 Index#versionType = VersionType.EXTERNAL | VersionType.EXTERNAL_GTE");
        }

        request.setRefreshPolicy(WriteRequest.RefreshPolicy.valueOf(delete.refreshPolicy().name()))
                .waitForActiveShards(ActiveShardCountUtils.getActiveShardCount(delete.waitForActiveShards()))
                .versionType(versionType)
                .timeout(delete.timeout());
    }

    @Override
    protected DeleteRequest doCreate(MethodMeta meta, Object[] args) {
        DeleteRequest request = Requests.deleteRequest(meta.getIndex(meta, args));

        ParameterMeta parameterMeta = meta.getConditionParameter();
        Object condition = parameterMeta.getValue(args);

        if (parameterMeta.isBasic()) {
            request.id(String.valueOf(condition));
        } else if (condition instanceof IdProvider) {
            request.id(((IdProvider) condition).id());
        } else {
            throw new ConditionNotSupportException("必须要提供文档 id，入参要么是基本类型，要么必须实现 IdProvider 接口：" + meta);
        }


        if (condition instanceof VersionProvider) {
            request.version(((VersionProvider) condition).version());
        }

        if (condition instanceof RoutingProvider) {
            request.routing(((RoutingProvider) condition).routing());
        }

        if (condition instanceof ReplicaVersionProvider) {
            val provider = (ReplicaVersionProvider) condition;
            request.setIfSeqNo(provider.seqNo());
            request.setIfPrimaryTerm(provider.primaryTerm());
        }

        if (condition instanceof ParentTaskProvider) {
            val provider = (ParentTaskProvider) condition;
            request.setParentTask(provider.nodeId(), provider.taskId());
        }

        return request;
    }
}
