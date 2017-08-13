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
package chat.web.servlet;

import chat.ejb.service.AccountService;
import java.io.IOException;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This HttpServlet handles requests to activate user accounts.
 *
 * @author Bryan Daniel
 */
@WebServlet(name = "AccountServlet", urlPatterns = {"/account"})
public class AccountServlet extends HttpServlet {

    /**
     * Serial version UID
     */
    private static final long serialVersionUID = -9186241764367869280L;

    /**
     * The account service
     */
    @EJB(beanName = "accountService")
    private AccountService accountService;

    /**
     * This GET method will activate an account associated with the 'code'
     * parameter value. If 'action' or 'code' parameters are missing or the
     * action value is not 'activate', the request will be forwarded to the
     * index page with no action performed.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        String code = request.getParameter("code");
        String url = "/index.xhtml";

        if (action != null && code != null && action.equals("activate")) {
            if (accountService.findActivationStatusByRegistrationCode(code)) {
                request.setAttribute("message", "Your account has already been activated.  "
                        + "You may sign in.");
            } else {
                if (accountService.activateAccount(code)) {
                    request.setAttribute("message", "Thank you! You have successfully activated "
                            + "your account.  You may now sign in.");
                } else {
                    request.setAttribute("message", "No account could be activated for the "
                            + "given registration code.");
                }
            }
        }
        getServletContext().getRequestDispatcher(url)
                .forward(request, response);
    }
}
