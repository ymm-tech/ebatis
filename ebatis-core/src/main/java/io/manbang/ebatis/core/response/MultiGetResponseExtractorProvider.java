package io.manbang.ebatis.core.response;

import com.google.auto.service.AutoService;
import com.google.common.collect.Lists;
import io.manbang.ebatis.core.generic.GenericType;
import io.manbang.ebatis.core.meta.MethodMeta;
import io.manbang.ebatis.core.meta.RequestType;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetResponse;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author weilong.hu
 * @since 2020/7/2 10:58
 */
@AutoService(ResponseExtractorProvider.class)
public class MultiGetResponseExtractorProvider extends AbstractResponseExtractorProvider {
    public MultiGetResponseExtractorProvider() {
        super(RequestType.MULTI_GET);
    }

    @Override
    protected ResponseExtractor<?> getResponseExtractor(MethodMeta meta, GenericType genericType) {
        Class<?> resultClass = genericType.resolve();
        if (MultiGetResponse.class == resultClass) {
            return RawResponseExtractor.INSTANCE;
        } else if (resultClass.isArray()) {
            Class<?> entityClass = genericType.resolveGeneric(0);
            if (MultiGetItemResponse.class == entityClass) {
                return response -> ((MultiGetResponse) response).getResponses();
            } else if (Optional.class == entityClass) {
                Class<?> entityClazz = genericType.resolveGeneric(0, 0);
                return response -> Stream.of(MultiGetResponseExtractor.of(entityClazz).doExtractData((MultiGetResponse) response))
                        .map(Optional::ofNullable).toArray(i -> (Optional<?>[]) Array.newInstance(Optional.class, i));
            } else {
                return MultiGetResponseExtractor.of(entityClass);
            }
        } else if (List.class.isAssignableFrom(resultClass)) {
            Class<?> entityClass = genericType.resolveGeneric(0);
            if (MultiGetItemResponse.class == entityClass) {
                return response -> Lists.newArrayList(((MultiGetResponse) response).getResponses());
            } else if (Optional.class == entityClass) {
                Class<?> entityClazz = genericType.resolveGeneric(0, 0);
                return response -> Stream.of(MultiGetResponseExtractor.of(entityClazz).doExtractData((MultiGetResponse) response))
                        .map(Optional::ofNullable).collect(Collectors.toList());
            } else {
                return response -> Lists.newArrayList(MultiGetResponseExtractor.of(entityClass).doExtractData((MultiGetResponse) response));
            }
        } else {
            throw new UnsupportedOperationException("暂不支持的返回值类型");
        }
    }
}
