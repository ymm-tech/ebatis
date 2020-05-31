package com.ymm.ebatis.core.response;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ymm.ebatis.core.meta.RequestType;
import com.ymm.ebatis.core.meta.ResultType;
import org.elasticsearch.action.get.GetResponse;
import org.springframework.core.ResolvableType;

import java.io.IOException;

public abstract class AbstractGetResponseExtractorProvider extends AbstractResponseExtractorProvider {
    private final ObjectMapper objectMapper;

    AbstractGetResponseExtractorProvider(ResultType... resultTypes) {
        super(RequestType.GET, resultTypes);
        this.objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
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
                        return objectMapper.readValue(response.getSourceAsBytes(), resultClass);
                    } else {
                        return null;
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            };
        }
    }
}
