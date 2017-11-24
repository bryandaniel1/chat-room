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
package chat.web.bean;

import chat.ejb.entity.ChatRoom;
import chat.ejb.entity.ChatRoomUser;
import chat.ejb.entity.Message;
import chat.ejb.service.ChatRoomService;
import chat.ejb.service.UserService;
import chat.web.bean.MessageDispatcher.MediaType;
import chat.web.model.ProcessingStatus;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.websocket.Session;

/**
 * This managed bean manages the HTTP sessions, web-socket sessions, and chat
 * room congregations for users of the chat room application.
 *
 * @author Bryan Daniel
 */
@Named("chatRoomManager")
@ApplicationScoped
public class ChatRoomManager implements Serializable {

    /**
     * Serial version UID
     */
    private static final long serialVersionUID = 1633106063313266247L;

    /**
     * The string indicating a user hosting a room
     */
    public static final String HOST_ROLE = "host";

    /**
     * The string indicating a guest
     */
    public static final String GUEST_ROLE = "guest";

    /**
     * The map of active chat rooms
     */
    private Map<ChatRoom, ArrayList<Session>> activeChatRooms;

    /**
     * The chat room users mapped by WebSocket session
     */
    private Map<Session, ChatRoomUser> websocketConnections;

    /**
     * The chat room users mapped by HTTP session ID
     */
    private Map<String, ChatRoomUser> httpSessionConnections;

    /**
     * The message dispatcher
     */
    @Inject
    private MessageDispatcher messageDispatcher;

    /**
     * The invitation manager
     */
    @Inject
    private InvitationManager invitationManager;

    /**
     * The chat room service
     */
    @EJB(beanName = "chatRoomService")
    private ChatRoomService chatRoomService;

    /**
     * The user service
     */
    @EJB(beanName = "userService")
    private UserService userService;

    /**
     * This method constructs the maps for active chat rooms, WebSocket
     * connections, and HTTP session connections.
     */
    @PostConstruct
    public void init() {
        activeChatRooms = new ConcurrentHashMap<>();
        websocketConnections = new ConcurrentHashMap<>();
        httpSessionConnections = new ConcurrentHashMap<>();
    }

    /**
     * This method opens a chat room for a user and adds it to the chat rooms
     * map.
     *
     * @param session the user session
     * @param roomName the room name
     * @param username the username
     */
    public void openChatRoom(Session session, String roomName, String username) {

        Logger.getLogger(ChatRoomManager.class.getName()).log(Level.INFO, "opening chat room: {0}", roomName);

        ProcessingStatus status = new ProcessingStatus();
        status.setStatus(ProcessingStatus.SUCCESS);
        ChatRoomUser user = findChatRoomUser(username);
        inspectChatRoomUser(user, status);

        if (status.getStatus().equals(ProcessingStatus.ERROR)) {
            Logger.getLogger(ChatRoomManager.class.getName()).log(Level.INFO, status.getDetails());
        } else {
            websocketConnections.put(session, user);

            ChatRoom room = chatRoomService.getChatRoom(roomName, user);
            if (user.getUsername().equals(room.getRoomCreator().getUsername())) {
                ArrayList<Session> newGroup = new ArrayList<>();
                newGroup.add(session);
                activeChatRooms.put(room, newGroup);
                Message m = new Message();
                m.setId(null);
                m.setImage(false);
                m.setRoomName(room);
                m.setTimeWritten(new Date());
                m.setUsername(user);
                m.setMessage(messageDispatcher.getEnterSubstring() + " " + username
                        + " has opened the chat room.");
                chatRoomService.storeMessage(m);
                messageDispatcher.sendMessageToGroup(activeChatRooms.get(room), m);
                Logger.getLogger(ChatRoomManager.class.getName()).log(Level.INFO, "{0} has been opened", roomName);
            } else {
                Logger.getLogger(ChatRoomManager.class.getName()).log(Level.INFO,
                        "The opening of a chat room failed for {0}.", username);
            }
        }
    }

    /**
     * This method joins a guest user to the chat room identified by the given
     * name.
     *
     * @param session the representative session
     * @param roomName the room name
     * @param username the username
     */
    public void joinChatRoom(Session session, String roomName, String username) {

        ProcessingStatus status = new ProcessingStatus();
        status.setStatus(ProcessingStatus.SUCCESS);
        ChatRoomUser user = findChatRoomUser(username);
        inspectChatRoomUser(user, status);

        if (status.getStatus().equals(ProcessingStatus.ERROR)) {
            Logger.getLogger(ChatRoomManager.class.getName()).log(Level.INFO, status.getDetails());
        } else {
            websocketConnections.put(session, user);

            ChatRoom chatRoom = null;
            for (ChatRoom r : activeChatRooms.keySet()) {
                if (r.getRoomName().equals(roomName)) {
                    chatRoom = r;
                    activeChatRooms.get(r).add(session);
                    Message m = new Message();
                    m.setId(null);
                    m.setImage(false);
                    m.setRoomName(r);
                    m.setTimeWritten(new Date());
                    m.setUsername(user);
                    m.setMessage(messageDispatcher.getEnterSubstring() + " "
                            + username + " has joined the chat.");
                    chatRoomService.storeMessage(m);
                    messageDispatcher.sendMessageToGroup(activeChatRooms.get(r), m);
                    break;
                }
            }

            if (chatRoom == null) {
                Logger.getLogger(ChatRoomManager.class.getName()).log(Level.INFO,
                        "The user, {0}, attempted to join a closed room.", username);
            }
        }
    }

    /**
     * This method handles the messages from a chat participant by locating the
     * chat room for the conversation, building a message object to be stored in
     * the database, and relaying the message to all chat participants in the
     * room.
     *
     * @param session the session sending the message
     * @param message the message
     * @param mediaType the media type
     */
    public void continueConversation(Session session, String message, MediaType mediaType) {

        ChatRoom conversation = null;
        for (ChatRoom cr : activeChatRooms.keySet()) {
            if (activeChatRooms.get(cr).contains(session)) {
                conversation = cr;
                break;
            }
        }

        if (conversation != null) {

            ChatRoomUser user = websocketConnections.get(session);
            Message m = new Message();
            m.setId(null);
            switch (mediaType) {
                case IMAGE:
                    m.setImage(true);
                    m.setVideo(false);
                    break;
                case VIDEO:
                    m.setImage(false);
                    m.setVideo(true);
                    break;
                default:
                    m.setImage(false);
                    m.setVideo(false);
                    break;
            }
            m.setRoomName(conversation);
            m.setTimeWritten(new Date());
            m.setUsername(user);
            m.setMessage(messageDispatcher.getChatSubstring() + " " + user.getUsername() + ": " + message);
            chatRoomService.storeMessage(m);
            messageDispatcher.sendMessageToGroup(activeChatRooms.get(conversation), m);
        } else {

            Message response = new Message();
            response.setMessage("The chat session was not found.");
            messageDispatcher.sendMessage(session, response);
        }
    }

    /**
     * This method removes the given user session from all chat rooms and
     * notifies the associated chat room users via message. If the user is the
     * creator of the chat room being vacated, the room is closed in the
     * process.
     *
     * @param session the session
     */
    public void leaveChatRoom(Session session) {

        ChatRoomUser user = websocketConnections.get(session);

        for (ChatRoom room : activeChatRooms.keySet()) {
            if (activeChatRooms.get(room).contains(session)) {
                if (room.getRoomCreator().getUsername().equals(user.getUsername())) {
                    if (session.isOpen()) {
                        Message m = new Message();
                        m.setId(null);
                        m.setImage(false);
                        m.setRoomName(room);
                        m.setTimeWritten(new Date());
                        m.setMessage(messageDispatcher.getExitSubstring() + " " + user.getUsername()
                                + " has left and closed the chat room.");
                        m.setUsername(user);
                        messageDispatcher.sendMessageToGroup(activeChatRooms.get(room), m);
                        chatRoomService.storeMessage(m);
                    }
                    activeChatRooms.remove(room);
                    invitationManager.removeInvitationList(room.getRoomName());
                } else {
                    if (session.isOpen()) {
                        Message m = new Message();
                        m.setId(null);
                        m.setImage(false);
                        m.setRoomName(room);
                        m.setTimeWritten(new Date());
                        m.setMessage(messageDispatcher.getExitSubstring() + " " + user.getUsername()
                                + " has left the room.");
                        m.setUsername(user);
                        messageDispatcher.sendMessageToGroup(activeChatRooms.get(room), m);
                        chatRoomService.storeMessage(m);
                    }
                    activeChatRooms.get(room).remove(session);
                }
            }
        }
        websocketConnections.remove(session);
    }

    /**
     * Upon a user entering the lobby, this method adds that user to the map of
     * chat room users.
     *
     * @param httpSessionId the HTTP session ID
     * @param user the chat room user
     */
    public void enterChatRoomLobby(String httpSessionId, ChatRoomUser user) {

        httpSessionConnections.putIfAbsent(httpSessionId, user);
    }

    /**
     * This method performs initial checks before allowing a user to open a chat
     * room.
     *
     * @param user the user
     * @param roomName the room name
     * @param invitees the invited users
     * @return the processing status
     */
    public ProcessingStatus processHost(ChatRoomUser user, String roomName, ArrayList<String> invitees) {

        ProcessingStatus status = new ProcessingStatus();
        status.setStatus(ProcessingStatus.SUCCESS);
        inspectChatRoomUser(user, status);
        if (status.getStatus().equals(ProcessingStatus.ERROR)) {
            return status;
        }
        ChatRoom room = chatRoomService.getChatRoom(roomName, user);
        if (!user.getUsername().equals(room.getRoomCreator().getUsername())) {
            status.setStatus(ProcessingStatus.ERROR);
            status.setDetails("A chat room with that name is already established by another user.");
            return status;
        }
        invitationManager.addInvitationList(roomName, invitees);
        return status;
    }

    /**
     * This method performs initial checks before allowing a user to join a chat
     * room.
     *
     * @param user the user
     * @param roomName the room name
     * @return the processing status
     */
    public ProcessingStatus processGuest(ChatRoomUser user, String roomName) {

        ProcessingStatus status = new ProcessingStatus();
        status.setStatus(ProcessingStatus.SUCCESS);
        inspectChatRoomUser(user, status);
        if (status.getStatus().equals(ProcessingStatus.ERROR)) {
            return status;
        }
        ChatRoom chatRoom = null;
        for (ChatRoom r : activeChatRooms.keySet()) {
            if (r.getRoomName().equals(roomName)) {
                chatRoom = r;
            }
        }
        if (chatRoom == null) {
            status.setStatus(ProcessingStatus.ERROR);
            status.setDetails("The selected chat room is no longer open.");
            return status;
        }
        if (!invitationManager.verifyInvitation(roomName, user.getUsername())) {
            status.setStatus(ProcessingStatus.ERROR);
            status.setDetails("You have not been invited to this chat room.");
            return status;
        }
        return status;
    }

    /**
     * This method manages the task of creating and sending messages consisting
     * of uploaded media.
     *
     * @param httpSessionId the HTTP session ID
     * @param itemNumber the number designated for the media item
     * @param mediaType the media type
     */
    public void sendMediaMessage(String httpSessionId, Long itemNumber, MediaType mediaType) {

        ChatRoomUser user = httpSessionConnections.get(httpSessionId);
        for (Session session : websocketConnections.keySet()) {
            if (websocketConnections.get(session) == user) {
                String message = messageDispatcher.buildMediaMessage(user, itemNumber, mediaType);
                continueConversation(session, message, mediaType);
                break;
            }
        }
    }

    /**
     * This method removes a user from the HTTP session connections, WebSocket
     * session connections, and active chat rooms. The sign-out event is
     * recorded and the map counts are logged.
     *
     * @param httpSessionId the HTTP session ID
     */
    public void exitChatApplication(String httpSessionId) {

        if (httpSessionConnections.get(httpSessionId) != null) {
            for (Session session : websocketConnections.keySet()) {
                if (websocketConnections.get(session).getUsername()
                        .equals(httpSessionConnections.get(httpSessionId).getUsername())) {
                    leaveChatRoom(session);
                    logMapCountsOnHTTPSessionRemoval(httpSessionId);
                }
            }
            userService.recordSignOut(httpSessionConnections.get(httpSessionId).getUsername());
            httpSessionConnections.remove(httpSessionId);
        }
        logMapCountsOnHTTPSessionRemoval(httpSessionId);
    }

    /**
     * This method finds the user associated with the given username.
     *
     * @param userName the username
     * @return the user or null if no user is found
     */
    public ChatRoomUser findChatRoomUser(String userName) {

        for (ChatRoomUser user : httpSessionConnections.values()) {
            if (user.getUsername().equals(userName)) {
                return user;
            }
        }
        return null;
    }

    /**
     * This method is performed prior to allowing a user to open or join a chat
     * room. First, the method performs a check to verify the given user exists.
     * If the user is found, then the WebSocket connections are inspected to
     * verify that the user is not already associated with a chat.
     *
     * @param user the chat room user
     * @param status the processing status
     */
    public void inspectChatRoomUser(ChatRoomUser user, ProcessingStatus status) {

        if (user == null || user.getUsername() == null) {
            status.setStatus(ProcessingStatus.ERROR);
            status.setDetails("The chat room user is not found.");
        } else {
            for (Entry<Session, ChatRoomUser> entry : websocketConnections.entrySet()) {
                if (entry.getValue().equals(user)) {
                    status.setStatus(ProcessingStatus.ERROR);
                    status.setDetails("You were already in a chat room.  You are now returned to the lobby.");
                    leaveChatRoom(entry.getKey());
                }
            }
        }
    }

    /**
     * This method uses the given chat room name to find the host for the room.
     *
     * @param roomName the chat room name
     * @return the username of the host
     */
    public String findHostUsername(String roomName) {

        for (ChatRoom r : activeChatRooms.keySet()) {
            if (r.getRoomName().equals(roomName)) {
                return r.getRoomCreator().getUsername();
            }
        }
        return null;
    }

    /**
     * This method returns the usernames of the users in the named room.
     *
     * @param roomName the room name
     * @return the list of chat room usernames
     */
    public ArrayList<String> getUsernamesForRoom(String roomName) {

        ArrayList<String> usernames = new ArrayList<>();
        for (ChatRoom room : activeChatRooms.keySet()) {
            if (room.getRoomName().equals(roomName)) {
                for (Session session : activeChatRooms.get(room)) {
                    usernames.add(websocketConnections.get(session).getUsername());
                }
                break;
            }
        }
        return usernames;
    }

    /**
     * This method logs counts of the maps for active chat rooms, WebSocket
     * session connections, HTTP session connections, and chat room invitation
     * lists. This method is invoked when an HTTP session is removed.
     *
     * @param httpSessionId the HTTP session ID
     */
    private void logMapCountsOnHTTPSessionRemoval(String httpSessionId) {
        Logger.getLogger(ChatRoomManager.class.getName()).log(Level.INFO, "Objects associated with "
                + "session, {0}, have been removed. Current HTTP session connections count: {1} Current WebSocket "
                + "connection count: {2} Current active chat rooms count: {3} Current number of invitation lists: {4}",
                new Object[]{httpSessionId, httpSessionConnections.size(), websocketConnections.size(),
                    activeChatRooms.size(), invitationManager.reportInvitationListsSize()});
    }

    /**
     * This method returns the active chat rooms.
     *
     * @return the active chat rooms
     */
    public Map<ChatRoom, ArrayList<Session>> getActiveChatRooms() {
        return activeChatRooms;
    }

    /**
     * This method returns the host role indicator.
     *
     * @return the host role indicator
     */
    public String getHostRole() {
        return HOST_ROLE;
    }

    /**
     * This method returns the guest role indicator.
     *
     * @return the guest role indicator
     */
    public String getGuestRole() {
        return GUEST_ROLE;
    }
}
