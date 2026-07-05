package org.springframework.boot.httpquery.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.httpquery.HttpQueryMapping;
import org.springframework.boot.webmvc.autoconfigure.DispatcherServletAutoConfiguration;
import org.springframework.boot.webmvc.autoconfigure.WebMvcRegistrations;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * Drop this starter on the classpath of any Spring Boot 4.1 servlet
 * application and {@link HttpQueryMapping}-annotated handler
 * methods become reachable via the HTTP QUERY method — no manual wiring,
 * no on/off switch to flip. It only activates for servlet web apps that
 * actually have Spring MVC on the classpath, and every bean it contributes
 * backs off the moment the application defines its own (same
 * {@code @ConditionalOnMissingBean} pattern Boot's own auto-configuration
 * uses everywhere), so existing GET/POST/PUT/DELETE/PATCH handling is
 * untouched whether this jar is present or not.
 */
@AutoConfiguration(before = DispatcherServletAutoConfiguration.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnClass(DispatcherServlet.class)
public class HttpQueryAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public WebMvcRegistrations httpQueryWebMvcRegistrations() {
        return new WebMvcRegistrations() {
            @Override
            public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
                return new QueryHandlerMapping();
            }
        };
    }

    /**
     * Overrides Boot's auto-configured DispatcherServlet bean. Must run
     * before {@link DispatcherServletAutoConfiguration} (see class-level
     * {@code @AutoConfiguration(before = ...)}) so that autoconfig's own
     * {@code @ConditionalOnMissingBean(name = "dispatcherServlet")} backs off.
     * If the application already defines its own "dispatcherServlet" bean,
     * this one backs off instead, and QUERY support simply doesn't apply —
     * no crash, no property to disable it with.
     */
    @Bean(name = DispatcherServletAutoConfiguration.DEFAULT_DISPATCHER_SERVLET_BEAN_NAME)
    @ConditionalOnMissingBean(name = DispatcherServletAutoConfiguration.DEFAULT_DISPATCHER_SERVLET_BEAN_NAME)
    public DispatcherServlet dispatcherServlet() {
        return new QueryAwareDispatcherServlet();
    }
}
