package com.ymm.ebatis.request;

import com.ymm.ebatis.annotation.Update;
import com.ymm.ebatis.common.DslUtils;
import com.ymm.ebatis.core.domain.ScriptProvider;
import com.ymm.ebatis.core.domain.VersionProvider;
import com.ymm.ebatis.meta.MethodMeta;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;

import java.util.Optional;

/**
 * @author 章多亮
 * @since 2019/12/17 19:23
 */
public class UpdateRequestFactory extends AbstractRequestFactory<Update, UpdateRequest> {
    public static final UpdateRequestFactory INSTANCE = new UpdateRequestFactory();

    private UpdateRequestFactory() {
    }

    @Override
    protected void setOptionalMeta(UpdateRequest request, Update update) {
        request.routing(update.routing())
                .fetchSource(update.fetchSource())
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

        Object doc = args[0];

        if (DslUtils.isBasicClass(doc.getClass())) {
            request.id(String.valueOf(doc));
        } else {
            if (doc instanceof VersionProvider) {
                request.version(((VersionProvider) doc).getVersion());
            }

            // 脚本更新
            if (doc instanceof ScriptProvider) {
                request.script(((ScriptProvider) doc).getScript().toEsScript());
            } else {
                // Partial Document 更新
                IndexRequest indexRequest = IndexRequestFactory.INSTANCE.doCreate(meta, args);
                request.doc(indexRequest);
            }

            setIdAndVersion(doc, request::id, request::version);
        }

        return request;
    }
}
