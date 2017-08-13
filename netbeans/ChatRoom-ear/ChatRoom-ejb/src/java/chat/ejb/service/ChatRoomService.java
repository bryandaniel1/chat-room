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
