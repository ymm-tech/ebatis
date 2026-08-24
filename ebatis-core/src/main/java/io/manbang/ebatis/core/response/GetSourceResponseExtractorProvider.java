package io.manbang.ebatis.core.response;

import com.google.auto.service.AutoService;
import io.manbang.ebatis.core.common.ObjectMapperHolder;
import io.manbang.ebatis.core.exception.DocumentDeserializeException;
import io.manbang.ebatis.core.generic.GenericType;
import io.manbang.ebatis.core.meta.MethodMeta;
import io.manbang.ebatis.core.meta.RequestType;
import org.elasticsearch.client.core.GetSourceResponse;

import java.util.Map;
import java.util.Optional;

@AutoService(ResponseExtractorProvider.class)
public class GetSourceResponseExtractorProvider extends AbstractResponseExtractorProvider {
    public GetSourceResponseExtractorProvider() {
        super(RequestType.GET_SOURCE);
    }

    @Override
    protected ResponseExtractor<?> getResponseExtractor(MethodMeta meta, GenericType genericType) {
        Class<?> resultClass = genericType.resolve();
        if (GetSourceResponse.class == resultClass) {
            return response -> value(response);
        } else if (Map.class.isAssignableFrom(resultClass)) {
            return response -> value(response).getSource();
        } else if (String.class == resultClass) {
            return response -> {
                try {
                    return ObjectMapperHolder.objectMapper().writeValueAsString(value(response).getSource());
                } catch (com.fasterxml.jackson.core.JsonProcessingException exception) {
                    throw new DocumentDeserializeException(exception);
                }
            };
        } else if (Optional.class == resultClass) {
            Class<?> entityClass = genericType.resolveGeneric(0);
            return response -> Optional.ofNullable(ObjectMapperHolder.objectMapper()
                    .convertValue(value(response).getSource(), entityClass));
        } else if (void.class == resultClass || Void.class == resultClass) {
            return VoidResponseExtractor.INSTANCE;
        }
        return response -> ObjectMapperHolder.objectMapper().convertValue(value(response).getSource(), resultClass);
    }

    @SuppressWarnings("unchecked")
    private static GetSourceResponse value(org.elasticsearch.action.ActionResponse response) {
        return ((ValueResponse<GetSourceResponse>) response).getValue();
    }
}
