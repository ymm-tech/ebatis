package io.manbang.ebatis.core.extension;

import io.manbang.ebatis.core.cluster.Cluster;
import io.manbang.ebatis.core.meta.MethodMeta;
import io.manbang.ebatis.core.request.RequestFactory;
import io.manbang.ebatis.core.response.ResponseExtractor;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.ActionResponse;

/** Complete extension chain for operations not modeled by built-in annotations. */
public interface OperationExtension<R extends ActionRequest, S extends ActionResponse> {
    String name();

    RequestFactory<R> requestFactory();

    void execute(Cluster cluster, R request, ActionListener<S> listener);

    ResponseExtractor<?> responseExtractor(MethodMeta method);
}
