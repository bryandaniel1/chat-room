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

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * This web application life cycle listener can be utilized to perform tasks on
 * application startup and shutdown. Currently, the startup and shutdown events
 * are logged.
 *
 * @author Bryan Daniel
 */
@WebListener
public class ChatRoomListener implements ServletContextListener {

    /**
     * This method logs the context initialization event.
     *
     * @param sce the ServletContextEvent object
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        Logger.getLogger(ChatRoomListener.class.getName()).log(Level.INFO,
                "ChatRoomListener.contextInitialized: ChatRoom application running.");
    }

    /**
     * This method logs the destruction of the context.
     *
     * @param sce the ServletContextEvent object
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        Logger.getLogger(ChatRoomListener.class.getName()).log(Level.INFO,
                "ChatRoomListener.contextDestroyed: ChatRoom application shut down.");
    }
}
