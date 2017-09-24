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
import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This filter clears the browser cache.
 *
 * @author Bryan Daniel
 */
@WebFilter(filterName = "HeadersFilter", urlPatterns = {"/signin/*", "/lobby/*", "/chat/*", "/admin/*"},
        dispatcherTypes = {DispatcherType.REQUEST, DispatcherType.FORWARD,
            DispatcherType.ERROR, DispatcherType.INCLUDE})
public class HeadersFilter implements Filter {

    /**
     * The debug indicator
     */
    private static final boolean debug = true;

    /**
     * The filter configuration
     */ 
    private FilterConfig filterConfig = null;

    /**
     * This method is invoked before the filter function.
     *
     * @param request the request object
     * @param response the response object
     * @throws IOException
     * @throws ServletException
     */
    private void doBeforeProcessing(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
        if (debug) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            log(MessageFormat.format("HeadersFilter.doBeforeProcessing: {0} requesting resource {1}",
                    httpRequest.getRemoteAddr(), httpRequest.getRequestURL().toString()));
        }
    }

    /**
     * This method is invoked after the filter function.
     *
     * @param request the request object
     * @param response the response object
     * @throws IOException
     * @throws ServletException
     */
    private void doAfterProcessing(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
        if (debug) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            log(MessageFormat.format("HeadersFilter.doAfterProcessing: {0} requesting resource {1}",
                    httpRequest.getRemoteAddr(), httpRequest.getRequestURL().toString()));
        }
    }

    /**
     * This doFilter method is used to clear the browser cache.
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
        
        doBeforeProcessing(request, response);

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        httpResponse.setHeader("Pragma", "no-cache");
        httpResponse.setDateHeader("Expires", 0);        

        if (debug) {
            log(MessageFormat.format("HeadersFilter.doFilter: {0} requesting resource {1}",
                    httpRequest.getRemoteAddr(), httpRequest.getRequestURL().toString()));
        }

        chain.doFilter(request, response);

        doAfterProcessing(request, response);
    }

    /**
     * Return the filter configuration object for this filter.
     *
     * @return the filter configuration
     */
    public FilterConfig getFilterConfig() {
        return (this.filterConfig);
    }

    /**
     * Set the filter configuration object for this filter.
     *
     * @param filterConfig The filter configuration object
     */
    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    /**
     * Destroy method for this filter
     */
    @Override
    public void destroy() {
    }

    /**
     * Init method for this filter
     *
     * @param filterConfig the filter configuration
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
     * This method logs a message using the FilterConfig object.
     *
     * @param msg the message to log
     */
    public void log(String msg) {
        filterConfig.getServletContext().log(msg);
    }
}
