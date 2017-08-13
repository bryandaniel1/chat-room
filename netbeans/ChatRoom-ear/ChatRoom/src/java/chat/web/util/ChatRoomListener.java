package chat.web.util;

import chat.ejb.service.ChatRoomService;
import chat.ejb.service.UserService;
import javax.ejb.EJB;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Web application lifecycle listener.
 *
 * @author Bryan Daniel
 */
@WebListener
public class ChatRoomListener implements ServletContextListener {

    /**
     * The user service
     */
    @EJB
    private UserService userService;

    /**
     * The chat room service
     */
    @EJB
    private ChatRoomService chatRoomService;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        /*ChatRoomUser user = new ChatRoomUser();
        user.setUsername("fred1");
        user.setFirstName("Fred");
        user.setLastName("Flintsone");
        user.setUserRole("user");
        UserCredentials credentials = new UserCredentials();
        credentials.setUsername("fred1");
        credentials.setPassword(BCrypt.hashpw("fredpass", BCrypt.gensalt()));
        user.setUserCredentials(credentials);
        userService.persist(user);
        
        ChatRoom room = chatRoomService.getChatRoom("Fred's Room", user);
        
        Message m = new Message();
        m.setId(null);
        m.setImage(false);
        m.setRoomName(room);
        m.setTimeWritten(new Date());
        m.setMessage("This is a test message.");
        m.setUsername(user);
        chatRoomService.storeMessage(m);*/
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        //no cleanup
    }
}
