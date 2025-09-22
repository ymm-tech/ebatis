package io.manbang.ebatis.core.response;

import com.google.auto.service.AutoService;
import io.manbang.ebatis.core.common.ObjectMapperHolder;
import io.manbang.ebatis.core.domain.MetaSource;
import io.manbang.ebatis.core.exception.DocumentDeserializeException;
import io.manbang.ebatis.core.generic.GenericType;
import io.manbang.ebatis.core.meta.MethodMeta;
import io.manbang.ebatis.core.meta.RequestType;
import org.elasticsearch.action.get.GetResponse;

import java.io.IOException;
import java.util.Optional;


/**
 * 抽象Get响应抽提器
 *
 * @author 章多亮
 * @since 2020/06/01 16:28:49
 */
@AutoService(ResponseExtractorProvider.class)
public class GetResponseExtractorProvider extends AbstractResponseExtractorProvider {
    public GetResponseExtractorProvider() {
        super(RequestType.GET);
    }

    @Override
    protected ResponseExtractor<?> getResponseExtractor(MethodMeta meta, GenericType genericType) {
        Class<?> resultClass = genericType.resolve();

        if (resultClass == GetResponse.class) {
            return RawResponseExtractor.INSTANCE;
        } else if (resultClass == MetaSource.class) {
            return GetRequestMetaSourceResponseExtractor.INSTANCE;
        } else if (resultClass == Optional.class) {
            return optionalResponseExtractor(genericType);
        } else {
            return otherResponseExtractor(resultClass);
        }
    }

    private static ConcreteResponseExtractor<?, GetResponse> otherResponseExtractor(Class<?> resultClass) {
        return response -> {
            try {
                if (response.isExists()) {
                    return ObjectMapperHolder.objectMapper().readValue(response.getSourceAsBytes(), resultClass);
                } else {
                    return null;
                }
            } catch (IOException e) {
                throw new DocumentDeserializeException(e);
            }
        };
    }

    private static ConcreteResponseExtractor<?, GetResponse> optionalResponseExtractor(GenericType genericType) {
        Class<?> entityClass = genericType.resolveGeneric(0);
        return response -> {
            try {
                if (response.isExists()) {
                    return Optional.ofNullable(ObjectMapperHolder.objectMapper().readValue(response.getSourceAsBytes(),
                            entityClass));
                } else {
                    return Optional.empty();
                }
            } catch (IOException e) {
                throw new DocumentDeserializeException(e);
            }
        };
    }
}
