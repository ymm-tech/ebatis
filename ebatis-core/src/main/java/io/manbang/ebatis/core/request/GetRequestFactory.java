package io.manbang.ebatis.core.request;

import io.manbang.ebatis.core.annotation.Get;
import io.manbang.ebatis.core.domain.Sort;
import io.manbang.ebatis.core.exception.ConditionNotSupportException;
import io.manbang.ebatis.core.meta.MetaUtils;
import io.manbang.ebatis.core.meta.MethodMeta;
import io.manbang.ebatis.core.meta.ParameterMeta;
import io.manbang.ebatis.core.provider.IdProvider;
import io.manbang.ebatis.core.provider.RoutingProvider;
import io.manbang.ebatis.core.provider.SortProvider;
import io.manbang.ebatis.core.provider.SourceProvider;
import io.manbang.ebatis.core.provider.VersionProvider;
import lombok.val;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.client.Requests;
import org.elasticsearch.index.VersionType;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;

import java.util.Arrays;

class GetRequestFactory extends AbstractRequestFactory<Get, GetRequest> {
    static final GetRequestFactory INSTANCE = new GetRequestFactory();

    private GetRequestFactory() {
    }

    @Override
    protected void setAnnotationMeta(GetRequest request, Get get) {
        val versionType = VersionType.valueOf(get.versionType().name());
        if (request.version() >= 0 && versionType == VersionType.INTERNAL) {
            throw new IllegalArgumentException(String.format("提供了版本号: %s，版本类型就不能是内部版本类型： VersionType.INTERNAL，请设置 Get#versionType = VersionType.EXTERNAL | VersionType.EXTERNAL_GTE", request.version()));
        }

        request.realtime(get.realtime())
                .refresh(get.refresh())
                .versionType(versionType)
                .preference(StringUtils.trimToNull(get.preference()));
    }

    @Override
    protected GetRequest doCreate(MethodMeta meta, Object[] args) {
        GetRequest request = Requests.getRequest(meta.getIndex(meta, args));

        ParameterMeta parameterMeta = meta.getConditionParameter();
        Object parameter = parameterMeta.getValue(args);

        // 必须要提供 Id
        if (parameterMeta.isBasic()) {
            request.id(String.valueOf(parameter));
        } else if (parameter instanceof IdProvider) {
            request.id(((IdProvider) parameter).id());
        } else {
            throw new ConditionNotSupportException("必须提供文档 id，入参要么是基本类型，要么要实现 IdProvider 接口：" + meta);
        }

        setProviderMeta(meta, request, parameter);

        return request;
    }

    private static void setProviderMeta(MethodMeta meta, GetRequest request, Object parameter) {
        if (meta.unwrappedReturnType().map(MetaUtils::isBasic).orElse(false)) {
            request.fetchSourceContext(new FetchSourceContext(false));
        } else {
            if (parameter instanceof SourceProvider) {
                SourceProvider sourceProvider = (SourceProvider) parameter;
                request.fetchSourceContext(new FetchSourceContext(true, sourceProvider.getIncludeFields(), sourceProvider.getExcludeFields()));
            } else {
                val includeFields = meta.getIncludeFields();
                if (ArrayUtils.isEmpty(includeFields)) {
                    request.fetchSourceContext(new FetchSourceContext(false, ArrayUtils.EMPTY_STRING_ARRAY, ArrayUtils.EMPTY_STRING_ARRAY));
                } else {
                    request.fetchSourceContext(new FetchSourceContext(true, meta.getIncludeFields(), ArrayUtils.EMPTY_STRING_ARRAY));
                }
            }
        }

        if (parameter instanceof VersionProvider) {
            request.version(((VersionProvider) parameter).version());
        }

        if (parameter instanceof SortProvider) {
            val sorts = ((SortProvider) parameter).getSorts();
            val sortedFields = Arrays.stream(sorts)
                    .map(Sort::name)
                    .toArray(String[]::new);

            request.storedFields(sortedFields);
        }

        if (parameter instanceof RoutingProvider) {
            request.routing(((RoutingProvider) parameter).routing());
        }
    }
}
