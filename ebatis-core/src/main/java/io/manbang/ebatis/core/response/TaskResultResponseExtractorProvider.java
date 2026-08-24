package io.manbang.ebatis.core.response;

import com.google.auto.service.AutoService;
import io.manbang.ebatis.core.generic.GenericType;
import io.manbang.ebatis.core.meta.MethodMeta;
import io.manbang.ebatis.core.meta.RequestType;
import org.elasticsearch.client.tasks.GetTaskResponse;
import org.elasticsearch.tasks.TaskInfo;

import java.util.Optional;

@AutoService(ResponseExtractorProvider.class)
public class TaskResultResponseExtractorProvider extends AbstractResponseExtractorProvider {
    public TaskResultResponseExtractorProvider() {
        super(RequestType.TASK_RESULT);
    }

    @Override
    protected ResponseExtractor<?> getResponseExtractor(MethodMeta meta, GenericType genericType) {
        Class<?> resultClass = genericType.resolve();
        if (GetTaskResponse.class == resultClass) {
            return response -> value(response).orElse(null);
        } else if (Optional.class == resultClass) {
            return TaskResultResponseExtractorProvider::value;
        } else if (TaskInfo.class == resultClass) {
            return response -> value(response).map(GetTaskResponse::getTaskInfo).orElse(null);
        } else if (boolean.class == resultClass || Boolean.class == resultClass) {
            return response -> value(response).map(GetTaskResponse::isCompleted).orElse(false);
        } else if (void.class == resultClass || Void.class == resultClass) {
            return VoidResponseExtractor.INSTANCE;
        }
        throw new UnsupportedOperationException("Unsupported TaskResult return type: " + genericType);
    }

    @SuppressWarnings("unchecked")
    private static Optional<GetTaskResponse> value(org.elasticsearch.action.ActionResponse response) {
        return ((ValueResponse<Optional<GetTaskResponse>>) response).getValue();
    }
}
