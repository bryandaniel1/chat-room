package chat.web.bean;

import chat.ejb.entity.ChatRoomUser;
import chat.ejb.entity.Message;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;
import javax.enterprise.context.ApplicationScoped;
import javax.websocket.Session;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * This managed bean relays messages between chat room users.
 *
 * @author Bryan Daniel
 */
@Named(value = "messageDispatcher")
@ApplicationScoped
public class MessageDispatcher implements Serializable {

    /**
     * Indicates he media type sent in a message
     */
    public enum MediaType {

        TEXT, IMAGE, VIDEO
    }

    /**
     * Serial version UID
     */
    private static final long serialVersionUID = 4215081860307441377L;

    /**
     * The substring indicating an exit by the user
     */
    private static final String EXIT_SUBSTRING = "userExit";

    /**
     * The substring indicating a message from the user
     */
    private static final String CHAT_SUBSTRING = "chatMessage";

    /**
     * The substring indicating a new user entering the room
     */
    private static final String ENTER_SUBSTRING = "userEnter";

    /**
     * The object mapper
     */
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * This method sends the given message to a group of users.
     *
     * @param sessions the group of users
     * @param message the message sent to the user
     */
    public void sendMessageToGroup(ArrayList<Session> sessions, Message message) {

        for (Session s : sessions) {
            sendMessage(s, message);
        }
    }

    /**
     * This method sends the given message to the user session.
     *
     * @param session the session for the user
     * @param message the message sent to the user
     */
    public void sendMessage(Session session, Message message) {

        try {
            session.getBasicRemote().sendText(mapper.writeValueAsString(message));
        } catch (IOException ex) {
            Logger.getLogger(ChatRoomManager.class.getName()).log(Level.SEVERE,
                    "An IOException occurred in the sendMessage method.", ex);
        }
    }

    /**
     * This message builds a string to direct chat participants to the file
     * designated by the file number.
     *
     * @param user the user submitting the image
     * @param itemNumber the number of the media file
     * @param mediaType the media type
     * @return the message
     */
    public String buildMediaMessage(ChatRoomUser user, Long itemNumber, MediaType mediaType) {
        
        String message = null;
        
        switch(mediaType){
            case IMAGE:
                message = "image?action=view&user=" + user.getUsername() + "&image=" + itemNumber;
                break;
            case VIDEO:
                message = "video?user=" + user.getUsername() + "&video=" + itemNumber;
                break;
            default:
                break;
        }
        
        return message;
    }

    /**
     * This method returns the exit indicator.
     *
     * @return the exit indicator
     */
    public String getExitSubstring() {
        return EXIT_SUBSTRING;
    }

    /**
     * This method returns the chat message indicator.
     *
     * @return the chat message indicator
     */
    public String getChatSubstring() {
        return CHAT_SUBSTRING;
    }

    /**
     * This method returns the indicator of a user entering the chat room.
     *
     * @return the enter indicator
     */
    public String getEnterSubstring() {
        return ENTER_SUBSTRING;
    }
}
