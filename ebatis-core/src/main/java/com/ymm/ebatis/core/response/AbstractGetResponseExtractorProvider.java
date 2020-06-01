package com.ymm.ebatis.core.response;

import com.ymm.ebatis.core.exception.DocumentDeserializeException;
import com.ymm.ebatis.core.meta.RequestType;
import com.ymm.ebatis.core.meta.ResultType;
import org.elasticsearch.action.get.GetResponse;
import org.springframework.core.ResolvableType;

import java.io.IOException;

import static com.ymm.ebatis.core.response.ObjectMapperHolder.objectMapper;

/**
 * 抽象Get响应抽提器
 *
 * @author 章多亮
 * @since 2020/06/01 16:28:49
 */
public abstract class AbstractGetResponseExtractorProvider extends AbstractResponseExtractorProvider {
    AbstractGetResponseExtractorProvider(ResultType... resultTypes) {
        super(RequestType.GET, resultTypes);
    }

    @Override
    protected ResponseExtractor<?> getResponseExtractor(ResolvableType resolvedResultType) {
        Class<?> resultClass = resolvedResultType.resolve();

        if (resultClass == GetResponse.class) {
            return RawGetResponseExtractor.INSTANCE;
        } else {
            return (ConcreteResponseExtractor<?, GetResponse>) response -> {
                try {
                    if (response.isExists()) {
                        return objectMapper().readValue(response.getSourceAsBytes(), resultClass);
                    } else {
                        return null;
                    }
                } catch (IOException e) {
                    throw new DocumentDeserializeException(e);
                }
            };
        }
    }
}
