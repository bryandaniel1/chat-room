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

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.ApplicationScoped;

/**
 * The InvitationManager holds the invitation list for each chat room and
 * verifies user invitations to the rooms.
 *
 * @author Bryan Daniel
 */
@Named("invitationManager")
@ApplicationScoped
public class InvitationManager {

    /**
     * The map of chat rooms invitation lists
     */
    private Map<String, ArrayList<String>> invitationLists;

    /**
     * This method constructs the map for invitation lists.
     */
    @PostConstruct
    public void init() {
        invitationLists = new ConcurrentHashMap<>();
    }

    /**
     * This method adds the given list of invitations to the map with the chat
     * room name as the key.
     *
     * @param roomName the chat room name
     * @param invitations the list of invitations
     */
    public void addInvitationList(String roomName, ArrayList<String> invitations) {
        invitationLists.put(roomName, invitations);
    }

    /**
     * This method remove the list of invitations mapped by the given room name.
     *
     * @param roomName the chat room name
     */
    public void removeInvitationList(String roomName) {
        invitationLists.remove(roomName);
    }

    /**
     * This method determines if the user with the given username is invited to
     * the given room.
     *
     * @param roomName the chat room name
     * @param username the username of the user
     * @return the boolean indicating whether or not the user is invited to the
     * room
     */
    public boolean verifyInvitation(String roomName, String username) {
        return invitationLists.get(roomName).contains(username);
    }

    /**
     * This method returns the size of the invitation lists map.
     *
     * @return the size of the invitation lists map
     */
    public int reportInvitationListsSize() {
        return invitationLists.size();
    }
}
