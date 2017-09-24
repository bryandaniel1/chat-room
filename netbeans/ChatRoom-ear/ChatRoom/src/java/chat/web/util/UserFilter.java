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
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
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
import javax.servlet.http.HttpSession;

/**
 * This class filters traffic to ensure only authenticated users can access the
 * lobby and chat pages.
 *
 * @author Bryan Daniel
 */
@WebFilter(filterName = "UserFilter", urlPatterns = {"/lobby/*", "/chat/*", "/admin/*"},
        dispatcherTypes = {DispatcherType.REQUEST, DispatcherType.FORWARD,
            DispatcherType.ERROR, DispatcherType.INCLUDE})
public class UserFilter implements Filter {

    /**
     * Debug status
     */
    private static final boolean debug = true;

    /**
     * The filter configuration object
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
            log(MessageFormat.format("UserFilter.doBeforeProcessing: {0} requesting resource {1}",
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
            log(MessageFormat.format("UserFilter.doAfterProcessing: {0} requesting resource {1}",
                    httpRequest.getRemoteAddr(), httpRequest.getRequestURL().toString()));
        }
    }

    /**
     * This method redirects requests to the sign-in page if the signed-in user
     * is not stored in the session.
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

        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession(false);
        PrintWriter writer = httpResponse.getWriter();
        String signInPath = "/ChatRoom/index.xhtml";

        if (debug) {
            log(MessageFormat.format("UserFilter.doFilter: {0} requesting resource {1}",
                    httpRequest.getRemoteAddr(), httpRequest.getRequestURL().toString()));
        }

        Throwable problem = null;
        try {

            String userId = null;
            if (session != null) {
                userId = (String) session.getAttribute("userName");
            }
            if (userId != null) {
                chain.doFilter(request, response);
            } else {

                // redirecting user with null ID to the sign-in page
                if ("partial/ajax".equals(httpRequest.getHeader("Faces-Request"))) {
                    String version = "1.0";
                    String encoding = "UTF-8";
                    httpResponse.setContentType("text/xml");
                    writer.append(MessageFormat.format("<?xml version=\"{0}\" encoding=\"{1}\"?>", version, encoding))
                            .write(MessageFormat.format("<partial-response><redirect url=\"{0}\"></redirect></partial-response>", signInPath));
                } else {
                    httpResponse.sendRedirect(signInPath);
                }
            }
        } catch (IOException | ServletException t) {
            problem = t;
        }

        doAfterProcessing(request, response);

        if (problem != null) {
            if (problem instanceof ServletException) {
                throw (ServletException) problem;
            }
            if (problem instanceof IOException) {
                throw (IOException) problem;
            }
            sendProcessingError(problem, response);
        }
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
                log("UserFilter:Initializing filter");
            }
        }
    }

    /**
     * Return a String representation of this object.
     */
    @Override
    public String toString() {
        if (filterConfig == null) {
            return ("UserFilter()");
        }
        StringBuilder sb = new StringBuilder("UserFilter(");
        sb.append(filterConfig);
        sb.append(")");
        return (sb.toString());
    }

    /**
     * This method sends the error message in response to a problem.
     *
     * @param t the Throwable object
     * @param response the response
     */
    private void sendProcessingError(Throwable t, ServletResponse response) {
        String stackTrace = getStackTrace(t);

        if (stackTrace != null && !stackTrace.equals("")) {
            try {
                response.setContentType("text/html");
                try (PrintStream ps = new PrintStream(response.getOutputStream()); PrintWriter pw = new PrintWriter(ps)) {
                    pw.print("<html>\n<head>\n<title>Error</title>\n</head>\n<body>\n"); //NOI18N

                    // PENDING! Localize this for next official release
                    pw.print("<h1>The resource did not process correctly</h1>\n<pre>\n");
                    pw.print(stackTrace);
                    pw.print("</pre></body>\n</html>"); //NOI18N
                }
                response.getOutputStream().close();
            } catch (Exception ex) {
            }
        } else {
            try {
                try (PrintStream ps = new PrintStream(response.getOutputStream())) {
                    t.printStackTrace(ps);
                }
                response.getOutputStream().close();
            } catch (Exception ex) {
            }
        }
    }

    /**
     * This method returns the stack trace for the given error.
     *
     * @param t the Throwable object
     * @return the stack trace
     */
    public static String getStackTrace(Throwable t) {
        String stackTrace = null;
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            pw.close();
            sw.close();
            stackTrace = sw.getBuffer().toString();
        } catch (Exception ex) {
        }
        return stackTrace;
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
