package com.ymm.ebatis.core.request;

import com.ymm.ebatis.core.annotation.Get;
import com.ymm.ebatis.core.exception.ConditionNotSupportException;
import com.ymm.ebatis.core.meta.MethodMeta;
import com.ymm.ebatis.core.meta.ParameterMeta;
import com.ymm.ebatis.core.provider.IdProvider;
import com.ymm.ebatis.core.provider.VersionProvider;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.client.Requests;

class GetRequestFactory extends AbstractRequestFactory<Get, GetRequest> {
    static final GetRequestFactory INSTANCE = new GetRequestFactory();

    private GetRequestFactory() {
    }

    @Override
    protected void setAnnotationMeta(GetRequest request, Get get) {
        request.routing(StringUtils.trimToNull(get.routing()))
                .realtime(get.realtime())
                .refresh(get.refresh())
                .preference(StringUtils.trimToNull(get.preference()));
    }

    @Override
    protected GetRequest doCreate(MethodMeta meta, Object[] args) {
        GetRequest request = Requests.getRequest(meta.getIndex());

        ParameterMeta parameterMeta = meta.getConditionParameter();
        Object value = parameterMeta.getValue(args);

        if (parameterMeta.isBasic()) {
            request.id(String.valueOf(value));
        } else {
            if (value instanceof IdProvider) {
                request.id(((IdProvider) value).getId());
            } else {
                throw new ConditionNotSupportException(meta.toString());
            }

            if (value instanceof VersionProvider) {
                request.version(((VersionProvider) value).getVersion());
            }
        }

        return request;
    }
}
