package io.manbang.ebatis.core.request;

import io.manbang.ebatis.core.annotation.Update;
import io.manbang.ebatis.core.common.ActiveShardCountUtils;
import io.manbang.ebatis.core.meta.MethodMeta;
import io.manbang.ebatis.core.meta.ParameterMeta;
import io.manbang.ebatis.core.provider.IdProvider;
import io.manbang.ebatis.core.provider.ParentTaskProvider;
import io.manbang.ebatis.core.provider.ReplicaVersionProvider;
import io.manbang.ebatis.core.provider.RoutingProvider;
import io.manbang.ebatis.core.provider.ScriptProvider;
import lombok.val;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.update.UpdateRequest;

/**
 * @author 章多亮
 * @since 2019/12/17 19:23
 */
class UpdateRequestFactory extends AbstractRequestFactory<Update, UpdateRequest> {
    static final UpdateRequestFactory INSTANCE = new UpdateRequestFactory();

    private UpdateRequestFactory() {
    }

    @Override
    protected void setAnnotationMeta(UpdateRequest request, Update update) {
        request.fetchSource(update.fetchSource())
                .timeout(update.timeout())
                .waitForActiveShards(ActiveShardCountUtils.getActiveShardCount(update.waitForActiveShards()))
                .detectNoop(update.detectNoop())
                .docAsUpsert(update.docAsUpsert())
                .retryOnConflict(update.retryOnConflict())
                .setRefreshPolicy(WriteRequest.RefreshPolicy.valueOf(update.refreshPolicy().name()))
                .setRequireAlias(update.requireAlias())
                .scriptedUpsert(update.scriptedUpsert());
    }

    @Override
    protected UpdateRequest doCreate(MethodMeta meta, Object[] args) {
        UpdateRequest request = new UpdateRequest();
        request.index(meta.getIndex(meta, args));

        ParameterMeta parameterMeta = meta.getConditionParameter();
        Object doc = parameterMeta.getValue(args);

        if (doc instanceof IdProvider) {
            request.id(((IdProvider) doc).id());
        }

        // 脚本更新
        if (doc instanceof ScriptProvider) {
            request.script(((ScriptProvider) doc).getScript().toEsScript());
        } else {
            // Partial Document 更新
            IndexRequest indexRequest = RequestFactory.index().create(meta, args);
            request.doc(indexRequest);
        }

        if (doc instanceof ReplicaVersionProvider) {
            val provider = (ReplicaVersionProvider) doc;
            request.setIfPrimaryTerm(provider.primaryTerm());
            request.setIfSeqNo(provider.seqNo());
        }

        if (doc instanceof RoutingProvider) {
            request.routing(((RoutingProvider) doc).routing());
        }

        if (doc instanceof ParentTaskProvider) {
            val provider = ((ParentTaskProvider) doc);
            request.setParentTask(provider.nodeId(), provider.taskId());
        }

        return request;
    }
}
