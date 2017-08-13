package chat.ejb.service;

import chat.ejb.entity.ChatRoomUser;
import java.util.List;
import javax.ejb.Local;

/**
 * This interface provides the functionality for operations concerning users of
 * the Enterprise WebSocket Chat application.
 *
 * @author Bryan Daniel
 */
@Local
public interface UserService {

    /**
     * This method takes the username and password parameters and verifies a
     * match in the database.
     *
     * @param user the chat room user
     * @return the indication of authentication
     */
    public boolean authenticateUser(ChatRoomUser user);
    
    /**
     * This method uses the entity manager to retrieve the user associated with
     * the given username.
     *
     * @param username the username
     * @return the user or null if no user could be found
     */
    public ChatRoomUser findUserByUsername(String username);
    
    /**
     * This method returns all chat room users from the database.
     * 
     * @return the list of all users
     */
    public List<ChatRoomUser> findAllUsers();
    
    /**
     * This method stores a user sign-in event in the database.
     *
     * @param username the username
     */
    public void recordSignIn(String username);
    
    /**
     * This method stores a user sign-out event in the database.
     *
     * @param username the username
     */
    public void recordSignOut(String username);
    
    /**
     * This method uses the entity manager to save a chat room user.
     *
     * @param user the chat room user
     * @return the indication of operation success or failure
     */
    public boolean persist(ChatRoomUser user);
}
