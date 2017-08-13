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
package chat.web.model;

import chat.ejb.entity.ChatRoomUser;

/**
 * This class builds and delivers email messengers.
 *
 * @author Bryan Daniel
 */
public class EmailMessengerFactory {

    /**
     * This method passes the ChatRoomUser and the registration code to the
     * constructor of the RegistrationEmailMessenger object and returns this new
     * object.
     *
     * @param recipient the user
     * @param registrationCode the registration code
     * @return the email messenger
     */
    public EmailMessenger produceRegistrationEmailMessenger(ChatRoomUser recipient,
            String registrationCode) {
        return new RegistrationEmailMessenger(recipient, registrationCode);
    }
    
    /**
     * This method passes the ChatRoomUser and the administrator message to the
     * constructor of the AdministratorEmailMessenger object and returns this new
     * object.
     *
     * @param recipient the user
     * @param message the message
     * @return the email messenger
     */
    public EmailMessenger produceAdministratorEmailMessenger(ChatRoomUser recipient,
            String message) {
        return new AdministratorEmailMessenger(recipient, message);
    }
    
}
