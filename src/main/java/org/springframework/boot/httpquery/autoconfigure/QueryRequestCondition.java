package org.springframework.boot.httpquery.autoconfigure;

import jakarta.servlet.http.HttpServletRequest;
import org.jspecify.annotations.Nullable;
import org.springframework.web.servlet.mvc.condition.RequestCondition;

/**
 * Matches requests whose raw HTTP method is QUERY. Bypasses the
 * {@code RequestMethod} enum entirely since it has no QUERY constant.
 */
public class QueryRequestCondition implements RequestCondition<QueryRequestCondition> {

    static final String QUERY_METHOD = "QUERY";

    @Override
    public QueryRequestCondition combine(final QueryRequestCondition other) {
        return this;
    }

    @Override
    public @Nullable QueryRequestCondition getMatchingCondition(final HttpServletRequest request) {
        return QUERY_METHOD.equalsIgnoreCase(request.getMethod()) ? this : null;
    }

    @Override
    public int compareTo(final QueryRequestCondition other, final HttpServletRequest request) {
        return 0;
    }

    @Override
    public boolean equals(final @Nullable Object obj) {
        return obj instanceof QueryRequestCondition;
    }

    @Override
    public int hashCode() {
        return QUERY_METHOD.hashCode();
    }

    @Override
    public String toString() {
        return QUERY_METHOD;
    }
}
