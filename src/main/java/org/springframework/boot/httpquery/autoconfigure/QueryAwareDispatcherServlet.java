package org.springframework.boot.httpquery.autoconfigure;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * DispatcherServlet subclass that force-routes QUERY requests to
 * {@code processRequest} directly, the same path FrameworkServlet uses for
 * PATCH. Belt-and-suspenders next to {@link QueryHandlerMapping}: without
 * this override, an unrecognized HTTP method can fall through to
 * {@code HttpServlet}'s default {@code service()} switch and get rejected
 * with 501 before Spring's handler mapping ever sees it.
 */
public class QueryAwareDispatcherServlet extends DispatcherServlet {

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        if (QueryRequestCondition.QUERY_METHOD.equalsIgnoreCase(request.getMethod())) {
            processRequest(request, response);
            return;
        }
        super.service(request, response);
    }
}
