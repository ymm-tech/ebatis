package com.ymm.ebatis.core.request;

import com.ymm.ebatis.core.annotation.SearchScroll;
import com.ymm.ebatis.core.meta.MethodMeta;
import com.ymm.ebatis.core.meta.ParameterMeta;
import com.ymm.ebatis.core.provider.ScrollProvider;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.search.ClearScrollRequest;
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
                return RequestFactory.search().create(meta, args).scroll(scroll.initialKeepAlive());
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
