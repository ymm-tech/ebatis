package io.manbang.ebatis.sample.web.resolver;

import io.manbang.ebatis.core.domain.Pageable;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Optional;

/**
 * @author 章多亮
 * @since 2020/6/1 18:53
 */
public class PageableMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType() == Pageable.class;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        Integer page = Optional.ofNullable(webRequest.getParameter("page"))
                .map(String::trim)
                .map(Integer::parseInt)
                .orElse(0);

        Integer size = Optional.ofNullable(webRequest.getParameter("size"))
                .map(String::trim)
                .map(Integer::parseInt)
                .orElse(20);

        return Pageable.of(page, size);
    }
}
