package io.manbang.ebatis.core.request;

import io.manbang.ebatis.core.annotation.SearchScroll;
import io.manbang.ebatis.core.meta.MethodMeta;
import io.manbang.ebatis.core.meta.ParameterMeta;
import io.manbang.ebatis.core.provider.RoutingProvider;
import io.manbang.ebatis.core.provider.ScrollProvider;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.search.ClearScrollRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.Requests;

import java.util.Collection;

/**
 * @author 章多亮
 * @since 2020/6/8 9:49
 */
class SearchScrollRequestFactory extends AbstractRequestFactory<SearchScroll, ActionRequest> {
    static final SearchScrollRequestFactory INSTANCE = new SearchScrollRequestFactory();

    private SearchScrollRequestFactory() {
    }

    @Override
    protected void setAnnotationMeta(ActionRequest request, SearchScroll scroll) {
        // do nothing
    }

    @Override
    protected ActionRequest doCreate(MethodMeta meta, Object[] args) {
        ParameterMeta parameter = meta.getConditionParameter();
        SearchScroll scroll = meta.getAnnotation(SearchScroll.class);

        Object condition = parameter.getValue(args);

        if (scroll.clearScroll()) {
            return getClearScrollRequest(parameter, condition);
        }


        // if condition is null, it means that current request is initial search request
        if (condition == null) {
            return RequestFactory.search().create(meta, args).scroll(scroll.initialKeepAlive());
        } else {
            ScrollProvider provider = (ScrollProvider) condition;
            String scrollId = provider.getScrollId();

            // if scrollId is null, it means that current request is initial search scroll request
            if (scrollId == null) {
                SearchRequest request = RequestFactory.search().create(meta, args).scroll(scroll.initialKeepAlive());
                if (condition instanceof RoutingProvider) {
                    request.routing(((RoutingProvider) condition).getRouting());
                }
                return request;
            } else {
                meta.setPageable(args);
                return Requests.searchScrollRequest(scrollId).scroll(StringUtils.trimToNull(scroll.keepAlive()));
            }
        }
    }

    private ActionRequest getClearScrollRequest(ParameterMeta parameter, Object condition) {
        ClearScrollRequest request = new ClearScrollRequest();

        if (parameter.isArray()) {
            for (Object scrollId : (Object[]) condition) {
                request.addScrollId(String.valueOf(scrollId));
            }
        } else if (parameter.isCollection()) {
            for (Object scrollId : (Collection<?>) condition) {
                request.addScrollId(String.valueOf(scrollId));
            }
        } else {
            request.addScrollId(String.valueOf(condition));
        }

        return request;
    }
}
