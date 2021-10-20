package io.manbang.ebatis.core.request;

import io.manbang.ebatis.core.annotation.Delete;
import io.manbang.ebatis.core.common.ActiveShardCountUtils;
import io.manbang.ebatis.core.exception.ConditionNotSupportException;
import io.manbang.ebatis.core.meta.MethodMeta;
import io.manbang.ebatis.core.meta.ParameterMeta;
import io.manbang.ebatis.core.provider.IdProvider;
import io.manbang.ebatis.core.provider.RoutingProvider;
import io.manbang.ebatis.core.provider.VersionProvider;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.client.Requests;

/**
 * @author 章多亮
 * @since 2019/12/17 19:20
 */
public class DeleteRequestFactory extends AbstractRequestFactory<Delete, DeleteRequest> {
    public static final DeleteRequestFactory INSTANCE = new DeleteRequestFactory();

    private DeleteRequestFactory() {
    }

    @Override
    protected void setAnnotationMeta(DeleteRequest request, Delete delete) {
        // TODO 还有很多参数没有设置
        request.setRefreshPolicy(delete.refreshPolicy())
                .waitForActiveShards(ActiveShardCountUtils.getActiveShardCount(delete.waitForActiveShards()))
                .versionType(delete.versionType())
                .timeout(delete.timeout());
    }

    @Override
    protected DeleteRequest doCreate(MethodMeta meta, Object[] args) {
        DeleteRequest request = Requests.deleteRequest(meta.getIndex(meta, args));
        setTypeIfNecessary(meta, args, request::type);

        ParameterMeta parameterMeta = meta.getConditionParameter();

        Object condition = parameterMeta.getValue(args);
        if (parameterMeta.isBasic()) {
            request.id(String.valueOf(condition));
        } else {
            if (condition instanceof IdProvider) {
                request.id(((IdProvider) condition).id());
            } else {
                throw new ConditionNotSupportException(meta.toString());
            }

            if (condition instanceof VersionProvider) {
                request.version(((VersionProvider) condition).version());
            }

            if (condition instanceof RoutingProvider) {
                request.routing(((RoutingProvider) condition).routing());
            }
        }

        return request;
    }
}
