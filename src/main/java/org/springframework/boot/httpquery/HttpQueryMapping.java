package org.springframework.boot.httpquery;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a handler method as reachable via the HTTP QUERY method (RFC 10008).
 * Not a composed {@code @RequestMapping} annotation on purpose — the mapping
 * is built manually in {@code org.springframework.boot.httpquery.autoconfigure.QueryHandlerMapping}
 * because {@code RequestMethod} has no QUERY constant to plug into the
 * standard {@code @RequestMapping(method = ...)} mechanism.
 *
 * <p>Combines with a class-level {@code @RequestMapping} base path the same
 * way {@code @GetMapping} etc. do.
 *
 * <p><b>Namespace note:</b> {@code org.springframework.boot.<feature>} +
 * {@code org.springframework.boot.<feature>.autoconfigure} is the exact
 * shape Boot's own modules use (e.g. {@code org.springframework.boot.webmvc}
 * / {@code org.springframework.boot.webmvc.autoconfigure}). This package is
 * new, not reused from a real Boot jar, so unlike the earlier
 * {@code org.springframework.web(.servlet.mvc)} placement, this one isn't a
 * split package with any real Spring artifact today — it only becomes one
 * if Spring ever ships a real {@code org.springframework.boot.httpquery}.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HttpQueryMapping {

    String[] value() default {};

    String[] consumes() default {};

    String[] produces() default {};
}
