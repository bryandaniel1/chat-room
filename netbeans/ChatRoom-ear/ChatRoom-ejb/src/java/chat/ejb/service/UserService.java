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
