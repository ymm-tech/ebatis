package io.manbang.ebatis.core.request;

import io.manbang.ebatis.core.annotation.MultiGet;
import io.manbang.ebatis.core.exception.ConditionNotSupportException;
import io.manbang.ebatis.core.meta.MethodMeta;
import io.manbang.ebatis.core.meta.ParameterMeta;
import io.manbang.ebatis.core.provider.IdProvider;
import io.manbang.ebatis.core.provider.RoutingProvider;
import io.manbang.ebatis.core.provider.VersionProvider;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.get.MultiGetRequest;
import org.elasticsearch.action.get.MultiGetRequest.Item;

import java.util.Collection;

/**
 * @author weilong.hu
 * @since 2020/7/2 10:48
 */
class MultiGetRequestFactory extends AbstractRequestFactory<MultiGet, MultiGetRequest> {
    static final MultiGetRequestFactory INSTANCE = new MultiGetRequestFactory();

    private MultiGetRequestFactory() {
    }

    @Override
    protected void setAnnotationMeta(MultiGetRequest request, MultiGet multiGet) {
        request.realtime(multiGet.realtime())
                .refresh(multiGet.refresh())
                .preference(StringUtils.trimToNull(multiGet.preference()));
    }

    @Override
    protected MultiGetRequest doCreate(MethodMeta meta, Object[] args) {
        ParameterMeta parameterMeta = meta.getConditionParameter();
        Object arg = parameterMeta.getValue(args);

        Object[] conditions;
        if (parameterMeta.isCollection()) {
            Collection<?> collection = (Collection<?>) arg;
            conditions = collection.toArray();
        } else if (parameterMeta.isArray()) {
            conditions = (Object[]) arg;
        } else {
            conditions = new Object[]{arg};
        }

        MultiGetRequest request = new MultiGetRequest();
        for (Object condition : conditions) {
            if (parameterMeta.isBasic()) {
                request.add(new Item(meta.getIndex(), StringUtils.trimToNull(meta.getType()), String.valueOf(condition)));
            } else {
                if (condition instanceof IdProvider) {
                    Item item = new Item(meta.getIndex(), StringUtils.trimToNull(meta.getType()), ((IdProvider) condition).id());
                    if (condition instanceof VersionProvider) {
                        item.version(((VersionProvider) condition).version());
                    }
                    if (condition instanceof RoutingProvider) {
                        item.routing(((RoutingProvider) condition).routing());
                    }
                    request.add(item);
                } else {
                    throw new ConditionNotSupportException(meta.toString());
                }
            }
        }
        return request;
    }
}
