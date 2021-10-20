package io.manbang.ebatis.core.request;

import io.manbang.ebatis.core.annotation.Get;
import io.manbang.ebatis.core.exception.ConditionNotSupportException;
import io.manbang.ebatis.core.meta.MethodMeta;
import io.manbang.ebatis.core.meta.ParameterMeta;
import io.manbang.ebatis.core.provider.IdProvider;
import io.manbang.ebatis.core.provider.RoutingProvider;
import io.manbang.ebatis.core.provider.VersionProvider;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.client.Requests;

class GetRequestFactory extends AbstractRequestFactory<Get, GetRequest> {
    static final GetRequestFactory INSTANCE = new GetRequestFactory();

    private GetRequestFactory() {
    }

    @Override
    protected void setAnnotationMeta(GetRequest request, Get get) {
        request.realtime(get.realtime())
                .refresh(get.refresh())
                .preference(StringUtils.trimToNull(get.preference()));
    }

    @Override
    protected GetRequest doCreate(MethodMeta meta, Object[] args) {
        GetRequest request = Requests.getRequest(meta.getIndex(meta, args));
        setTypeIfNecessary(meta, args, request::type);

        ParameterMeta parameterMeta = meta.getConditionParameter();
        Object value = parameterMeta.getValue(args);

        if (parameterMeta.isBasic()) {
            request.id(String.valueOf(value));
        } else {
            if (value instanceof IdProvider) {
                request.id(((IdProvider) value).id());
            } else {
                throw new ConditionNotSupportException(meta.toString());
            }

            if (value instanceof VersionProvider) {
                request.version(((VersionProvider) value).version());
            }

            if (value instanceof RoutingProvider) {
                request.routing(((RoutingProvider) value).routing());
            }
        }

        return request;
    }
}
