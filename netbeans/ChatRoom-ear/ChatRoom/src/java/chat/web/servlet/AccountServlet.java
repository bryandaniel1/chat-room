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
                boolean activated = accountService.activateAccount(code);
                if(activated){
                    request.setAttribute("message", "Thank you! You have successfully activated "
                        + "your account.  You may now sign in.");
                }else{
                    request.setAttribute("message", "No account could be activated for the "
                            + "given registration code.");
                }
            }
        }
        getServletContext().getRequestDispatcher(url)
                .forward(request, response);
    }
}
