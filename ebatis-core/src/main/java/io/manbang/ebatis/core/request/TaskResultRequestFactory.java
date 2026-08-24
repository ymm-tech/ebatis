package io.manbang.ebatis.core.request;

import io.manbang.ebatis.core.annotation.TaskResult;
import io.manbang.ebatis.core.meta.MethodMeta;
import org.elasticsearch.client.tasks.GetTaskRequest;
import org.elasticsearch.core.TimeValue;

class TaskResultRequestFactory extends AbstractRequestFactory<TaskResult, TaskResultActionRequest> {
    static final TaskResultRequestFactory INSTANCE = new TaskResultRequestFactory();

    private TaskResultRequestFactory() {
    }

    @Override
    protected void setAnnotationMeta(TaskResultActionRequest wrapper, TaskResult annotation) {
        wrapper.getRequest().setWaitForCompletion(annotation.waitForCompletion())
                .setTimeout(TimeValue.parseTimeValue(annotation.timeout(), "TaskResult.timeout"));
    }

    @Override
    protected TaskResultActionRequest doCreate(MethodMeta meta, Object[] args) {
        String taskId = String.valueOf(meta.getConditionParameter().getValue(args));
        int separator = taskId.lastIndexOf(':');
        if (separator <= 0 || separator == taskId.length() - 1) {
            throw new IllegalArgumentException("task id must use nodeId:number format: " + taskId);
        }
        return new TaskResultActionRequest(new GetTaskRequest(
                taskId.substring(0, separator), Long.parseLong(taskId.substring(separator + 1))));
    }
}
