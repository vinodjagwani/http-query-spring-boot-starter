package org.springframework.boot.httpquery.autoconfigure;

import java.lang.reflect.Method;
import org.jspecify.annotations.Nullable;
import org.springframework.boot.httpquery.HttpQueryMapping;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * Extends the standard handler mapping to also recognize {@link HttpQueryMapping}
 * methods. Registered by {@link HttpQueryAutoConfiguration} through Boot's
 * {@code WebMvcRegistrations} hook, so all other Boot auto-configuration for
 * MVC stays intact for the consuming application.
 */
public class QueryHandlerMapping extends RequestMappingHandlerMapping {

    @Override
    protected @Nullable RequestMappingInfo getMappingForMethod(final Method method, final Class<?> handlerType) {
        final HttpQueryMapping httpQuery = AnnotatedElementUtils.findMergedAnnotation(method, HttpQueryMapping.class);
        if (httpQuery == null) {
            return super.getMappingForMethod(method, handlerType);
        }

        final RequestMappingInfo methodInfo = buildQueryMappingInfo(httpQuery);

        final RequestMapping typeMapping = AnnotatedElementUtils.findMergedAnnotation(handlerType, RequestMapping.class);
        if (typeMapping != null) {
            final RequestMappingInfo typeInfo = createRequestMappingInfo(typeMapping, null);
            return typeInfo.combine(methodInfo);
        }

        return methodInfo;
    }

    private RequestMappingInfo buildQueryMappingInfo(final HttpQueryMapping httpQuery) {
        return RequestMappingInfo
                .paths(resolveEmbeddedValuesInPatterns(httpQuery.value()))
                .consumes(httpQuery.consumes())
                .produces(httpQuery.produces())
                .customCondition(new QueryRequestCondition())
                .options(getBuilderConfiguration())
                .build();
    }
}
