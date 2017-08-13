package chat.web.util;

import chat.web.bean.ChatRoomManager;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Web application HTTP session listener.
 *
 * @author Bryan Daniel
 */
@WebListener
public class ChatUserSessionListener implements HttpSessionListener {

    /**
     * The chat room manager
     */
    @Inject
    private ChatRoomManager manager;

    /**
     * Not implemented
     *
     * @param se
     */
    @Override
    public void sessionCreated(HttpSessionEvent se) {
    }

    /**
     * On session destruction, this method ensures that chat map objects are
     * cleansed of the values associated with the destroyed session.
     *
     * @param se the HttpSessionEvent object
     */
    @Override
    public void sessionDestroyed(HttpSessionEvent se) {

        Logger.getLogger(ChatUserSessionListener.class.getName()).log(Level.INFO,
                        "sessionDestroyed method called for session ID, {0}", se.getSession().getId());
        manager.exitChatApplication(se.getSession().getId());
    }
}
