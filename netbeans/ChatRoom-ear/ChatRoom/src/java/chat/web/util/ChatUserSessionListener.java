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
package chat.web.util;

import chat.web.bean.ChatRoomManager;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * This web application HTTP session listener performs tasks required on session
 * creation and destruction.
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
     * No functionality is currently provided by this method.
     *
     * @param se the HttpSessionEvent object
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
