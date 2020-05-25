package com.ymm.ebatis.request;

import com.ymm.ebatis.annotation.Delete;
import com.ymm.ebatis.common.DslUtils;
import com.ymm.ebatis.meta.MethodMeta;
import com.ymm.ebatis.provider.VersionProvider;
import org.elasticsearch.action.delete.DeleteRequest;

/**
 * @author 章多亮
 * @since 2019/12/17 19:20
 */
public class DeleteRequestFactory extends AbstractRequestFactory<Delete, DeleteRequest> {
    public static final DeleteRequestFactory INSTANCE = new DeleteRequestFactory();

    private DeleteRequestFactory() {
    }

    @Override
    protected void setOptionalMeta(DeleteRequest request, Delete delete) {
        request.setRefreshPolicy(delete.refreshPolicy())
                .timeout(delete.timeout())
                .routing(delete.routing());
    }

    @Override
    protected DeleteRequest doCreate(MethodMeta meta, Object[] args) {
        DeleteRequest request = new DeleteRequest();
        Object condition = args[0];

        if (condition instanceof VersionProvider) {
            request.version(((VersionProvider) condition).getVersion());
        }

        if (DslUtils.isBasicClass(condition.getClass())) {
            request.id(String.valueOf(condition));
        } else {
            setIdAndVersion(condition, request::id, request::version);
        }

        return request;
    }
}
