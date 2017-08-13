package chat.ejb.service;

import chat.ejb.entity.ChatRoom;
import chat.ejb.entity.ChatRoomUser;
import chat.ejb.entity.Message;
import chat.ejb.model.Conversation;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 * This chat room service interface provides the functionality for managing chat
 * room data.
 *
 * @author Bryan Daniel
 */
@Local
public interface ChatRoomService {

    /**
     * This method returns a ChatRoom object with the given name.
     *
     * @param roomName the chat room name
     * @param user the chat room user
     * @return the ChatRoom object
     */
    public ChatRoom getChatRoom(String roomName, ChatRoomUser user);
    
    /**
     * This method saves a message to the database.
     * 
     * @param message the message to save
     */
    public void storeMessage(Message message);
    
    /**
     * This method queries the database to find all conversations associated
     * with the given username.
     *
     * @param username the username
     * @return the list of conversations
     */
    public List<Conversation> findUserConversations(String username);
    
    /**
     * This method finds and returns all messages in a conversation associated
     * with the given day and room name.
     *
     * @param day the day
     * @param roomName the room name
     * @param user the user
     * @return the list of messages
     */
    public List<Message> getAllConversationMessages(Date day, String roomName, ChatRoomUser user);
}
