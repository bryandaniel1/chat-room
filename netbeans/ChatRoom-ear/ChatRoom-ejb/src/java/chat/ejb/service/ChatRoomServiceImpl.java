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
package chat.ejb.service;

import chat.ejb.entity.ChatRoom;
import chat.ejb.entity.ChatRoomUser;
import chat.ejb.entity.Message;
import chat.ejb.model.Conversation;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * This chat room service implementation provides the functionality for managing
 * chat room data.
 *
 * @author Bryan Daniel
 */
@Stateless(name = "chatRoomService")
public class ChatRoomServiceImpl implements ChatRoomService {

    /**
     * The entity manager for the chat room entities
     */
    @PersistenceContext(unitName = "ChatRoom-ejbPU")
    private EntityManager entityManager;

    /**
     * This method returns a ChatRoom object with the given name.
     *
     * @param roomName the chat room name
     * @param user the chat room user
     * @return the ChatRoom object
     */
    @Override
    public ChatRoom getChatRoom(String roomName, ChatRoomUser user) {

        Query query = entityManager.createNamedQuery("ChatRoom.findByRoomName");
        ChatRoom room = null;
        try {
            room = (ChatRoom) query.setParameter("roomName", roomName).getSingleResult();
        } catch (NoResultException nre) {
            Logger.getLogger(UserServiceImpl.class.getName()).log(Level.FINER, null, nre);
        } catch (Exception e) {
            Logger.getLogger(UserServiceImpl.class.getName()).log(Level.SEVERE, 
                    "An exception occurred in the getChatRoom method.", e);
        }
        if (room == null) {
            room = new ChatRoom();
            room.setRoomName(roomName);
            room.setRoomCreator(user);
            room.setTimeCreated(new Date());
            entityManager.persist(room);
        }

        return room;
    }

    /**
     * This method saves a message to the database.
     *
     * @param message the message to save
     */
    @Override
    public void storeMessage(Message message) {
        entityManager.persist(message);
    }

    /**
     * This method queries the database to find all conversations associated
     * with the given username.
     *
     * @param username the username
     * @return the list of conversations or null if none are returned
     */
    @Override
    public List<Conversation> findUserConversations(String username) {

        List<Conversation> conversations = null;

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT CAST(@row_number := @row_number + 1 AS DECIMAL) AS conversation_index, conversations.* ");
        stringBuilder.append("FROM (SELECT DATE(m.time_written) AS day, m.room_name AS room ");
        stringBuilder.append("FROM Message m ");
        stringBuilder.append("WHERE m.username = ? ");
        stringBuilder.append("GROUP BY day, room ");
        stringBuilder.append("ORDER BY day, room) conversations, (SELECT @row_number := 0) row_tally");
        Query query = entityManager.createNativeQuery(stringBuilder.toString(),
                "ConversationMapping");
        query.setParameter(1, username);
        
        try {
            conversations = query.getResultList();
        } catch (Exception e) {
            Logger.getLogger(UserServiceImpl.class.getName()).log(Level.SEVERE,
                    "An exception occurred in the findUserConversations method.", e);
        }

        return conversations;
    }

    /**
     * This method finds and returns all messages in a conversation associated
     * with the given day and room name.
     *
     * @param day the day
     * @param roomName the room name
     * @param user the user
     * @return the list of messages or null if none are returned
     */
    @Override
    public List<Message> getAllConversationMessages(Date day, String roomName, ChatRoomUser user) {

        Query query = entityManager.createNamedQuery("Message.findConversationMessages");
        List<Message> messages = null;
        
        // getting a Calendar set to provide the next day's date
        Calendar dayAfter = Calendar.getInstance();
        dayAfter.setTime(day);
        dayAfter.add(Calendar.DATE, 1);
        
        // getting the ChatRoom object
        ChatRoom room = getChatRoom(roomName, user);

        try {
            messages = query.setParameter("timeWritten", day).setParameter("dayAfter", dayAfter.getTime())
                    .setParameter("roomName", room).getResultList();
        } catch (Exception e) {
            Logger.getLogger(UserServiceImpl.class.getName()).log(Level.SEVERE,
                    "An exception occurred in the getAllConversationMessages method.", e);
        }

        return messages;
    }
}
