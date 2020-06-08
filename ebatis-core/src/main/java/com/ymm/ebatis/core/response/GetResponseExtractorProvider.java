package com.ymm.ebatis.core.response;

import com.google.auto.service.AutoService;
import com.ymm.ebatis.core.common.ObjectMapperHolder;
import com.ymm.ebatis.core.exception.DocumentDeserializeException;
import com.ymm.ebatis.core.meta.MethodMeta;
import com.ymm.ebatis.core.meta.RequestType;
import org.elasticsearch.action.get.GetResponse;
import org.springframework.core.ResolvableType;

import java.io.IOException;


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
    protected ResponseExtractor<?> getResponseExtractor(MethodMeta meta, ResolvableType resolvedResultType) {
        Class<?> resultClass = resolvedResultType.resolve();

        if (resultClass == GetResponse.class) {
            return RawResponseExtractor.INSTANCE;
        } else {
            return (ConcreteResponseExtractor<?, GetResponse>) response -> {
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
    }
}
