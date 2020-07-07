package com.ymm.ebatis.core.request;

import com.ymm.ebatis.core.annotation.Update;
import com.ymm.ebatis.core.common.ActiveShardCountUtils;
import com.ymm.ebatis.core.meta.MethodMeta;
import com.ymm.ebatis.core.meta.ParameterMeta;
import com.ymm.ebatis.core.provider.IdProvider;
import com.ymm.ebatis.core.provider.RoutingProvider;
import com.ymm.ebatis.core.provider.ScriptProvider;
import com.ymm.ebatis.core.provider.VersionProvider;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.support.single.instance.InstanceShardOperationRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.common.unit.TimeValue;

import java.util.Optional;

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
                .timeout(StringUtils.isBlank(update.timeout()) ? InstanceShardOperationRequest.DEFAULT_TIMEOUT :
                        TimeValue.parseTimeValue(update.timeout(), "更新超时时间"))
                .waitForActiveShards(ActiveShardCountUtils.getActiveShardCount(update.waitForActiveShards()))
                .detectNoop(update.detectNoop())
                .docAsUpsert(update.docAsUpsert())
                .retryOnConflict(update.retryOnConflict())
                .setRefreshPolicy(update.refreshPolicy())
                .scriptedUpsert(update.scriptedUpsert());

        // 如果指定的Id
        if (StringUtils.isNotBlank(update.id())) {
            // 并且是 Partial Document更新
            Optional.ofNullable(request.doc())
                    .ifPresent(doc -> doc.id(String.valueOf(doc.sourceAsMap().get(update.id()))));
        }
    }

    @Override
    protected UpdateRequest doCreate(MethodMeta meta, Object[] args) {
        UpdateRequest request = new UpdateRequest();
        request.index(meta.getIndex());
        setTypeIfNecessary(meta, request::type);

        ParameterMeta parameterMeta = meta.getConditionParameter();
        Object doc = parameterMeta.getValue(args);

        if (parameterMeta.isBasic()) {
            request.id(String.valueOf(doc));
        } else {
            if (doc instanceof VersionProvider) {
                request.version(((VersionProvider) doc).getVersion());
            }

            if (doc instanceof IdProvider) {
                request.id(((IdProvider) doc).getId());
            }

            // 脚本更新
            if (doc instanceof ScriptProvider) {
                request.script(((ScriptProvider) doc).getScript().toEsScript());
            } else {
                // Partial Document 更新
                IndexRequest indexRequest = RequestFactory.index().create(meta, args);
                request.doc(indexRequest);
            }

            if (doc instanceof RoutingProvider) {
                request.routing(((RoutingProvider) doc).getRouting());
            }
        }

        return request;
    }
}
