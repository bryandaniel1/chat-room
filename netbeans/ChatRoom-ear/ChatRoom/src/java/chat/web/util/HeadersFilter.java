/* 
 * Copyright 2017 Bryan Daniel.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package chat.web.util;

import java.io.IOException;
import java.text.MessageFormat;
import javax.faces.application.ViewExpiredException;
import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;

/**
 * This filter clears the browser cache.
 *
 * @author Bryan Daniel
 * @version 1, April 8, 2016
 */
@WebFilter(filterName = "HeadersFilter", urlPatterns = {"/*"},
        dispatcherTypes = {DispatcherType.REQUEST, DispatcherType.FORWARD,
            DispatcherType.ERROR, DispatcherType.INCLUDE})
public class HeadersFilter implements Filter {

    /**
     * Debug status
     */
    private static final boolean debug = false;

    /**
     * The filter configuration object we are associated with. If this value is
     * null, this filter instance is not currently configured.
     */
    private FilterConfig filterConfig = null;

    /**
     * This doFilter method is used to clear the browser cache. As this filter
     * is the first to handle requests, any request throwing a
     * ViewExpiredException is logged without printing a stack trace and
     * redirected to the expired page.
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param chain The filter chain we are processing
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        HttpServletResponse httpResponse = (HttpServletResponse) response;

        httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        httpResponse.setHeader("Pragma", "no-cache");
        httpResponse.setDateHeader("Expires", 0);

        try {
            chain.doFilter(request, response);
        } catch (ServletException se) {
            if (se.getRootCause().getClass().getName().equals(ViewExpiredException.class.getName())) {
                log(MessageFormat.format("HeadersFilter.doFilter: ViewExpiredException was thrown - {0}",
                        se.getMessage()));
                httpResponse.sendRedirect("/ChatRoom/expired.xhtml");
            } else {
                throw se;
            }
        }
    }

    /**
     * Destroy method for this filter
     */
    @Override
    public void destroy() {
        filterConfig = null;
    }

    /**
     * Init method for this filter
     *
     * @param filterConfig the filter configuration object
     */
    @Override
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
        if (filterConfig != null) {
            if (debug) {
                log("HeadersFilter:Initializing filter");
            }
        }
    }

    /**
     * Return a String representation of this object.
     */
    @Override
    public String toString() {
        if (filterConfig == null) {
            return ("HeadersFilter()");
        }
        StringBuilder sb = new StringBuilder("HeadersFilter(");
        sb.append(filterConfig);
        sb.append(")");
        return (sb.toString());
    }

    /**
     * The message logger method
     *
     * @param msg the message
     */
    public void log(String msg) {
        filterConfig.getServletContext().log(msg);
    }
}
