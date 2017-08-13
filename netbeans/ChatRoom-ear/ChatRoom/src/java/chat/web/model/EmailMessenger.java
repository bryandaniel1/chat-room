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

/**
 * This interface provides functionality to send an email message.
 *
 * @author Bryan Daniel
 */
public interface EmailMessenger {

    /**
     * This method sends an email from a host email address to a recipient's
     * address.
     *
     * @param recipientAddress the new user's email address
     * @return the indication of operation success or failure
     */
    public boolean sendMail(String recipientAddress);
}
