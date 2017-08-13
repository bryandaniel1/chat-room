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
package chat.web.websocket;

import chat.web.bean.ChatRoomManager;
import chat.web.bean.MessageDispatcher;
import chat.web.bean.MessageDispatcher.MediaType;
import chat.web.bean.UserBean;
import javax.inject.Inject;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

/**
 * This class handles the operations for a client's web socket communication.
 *
 * @author Bryan Daniel
 */
@ServerEndpoint("/chatroom/{roomName}/{username}/{chatRole}")
public class ChatEndpoint {

    /**
     * The chat session manager
     */
    @Inject
    private ChatRoomManager manager;

    /**
     * The message dispatcher
     */
    @Inject
    private MessageDispatcher messageDispatcher;

    /**
     * The user bean
     */
    @Inject
    private UserBean userBean;

    /**
     * Executes when the web socket opens.
     *
     * @param session the session
     * @param roomName the room name
     * @param username the user name
     * @param chatRole the role of the user
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("roomName") String roomName,
            @PathParam("username") String username, @PathParam("chatRole") String chatRole) {

        if (chatRole.equals(manager.getHostRole())) {
            manager.openChatRoom(session, roomName, username);
        } else {
            manager.joinChatRoom(session, roomName, username);
        }
    }

    /**
     * Executes on a web socket error.
     *
     * @param t the error
     */
    @OnError
    public void onError(Throwable t) {
    }

    /**
     * Executes when the web socket closes.
     *
     * @param session the session
     * @param reason the reason for closing
     */
    @OnClose
    public void onClose(Session session, CloseReason reason) {

        manager.leaveChatRoom(session);
        userBean.signOutUser();
    }

    /**
     * Executes to handle web socket messages.
     *
     * @param session the session
     * @param message the message
     */
    @OnMessage
    public void onMessage(Session session, String message) {

        if (message.startsWith(messageDispatcher.getExitSubstring())) {
            manager.leaveChatRoom(session);
        } else {
            manager.continueConversation(session, getMessageContents(message),
                    MediaType.TEXT);
        }
    }

    /**
     * This method returns the message contents from the given string.
     *
     * @param message the message string
     * @return the message contents
     */
    private String getMessageContents(String message) {
        return message.substring(messageDispatcher.getChatSubstring().length());
    }
}
