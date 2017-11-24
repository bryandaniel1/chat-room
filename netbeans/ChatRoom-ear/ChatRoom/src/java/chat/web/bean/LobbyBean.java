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
import chat.ejb.service.UserService;
import chat.web.model.ProcessingStatus;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;

/**
 * This managed bean supports the operations and UI components of the lobby
 * page.
 *
 * @author Bryan Daniel
 */
@Named(value = "lobbyBean")
@ViewScoped
public class LobbyBean implements Serializable {

    /**
     * Serial version UID
     */
    private static final long serialVersionUID = -5981183756230804332L;

    /**
     * The list of chat rooms
     */
    private ArrayList<ChatRoom> rooms;

    /**
     * The list of other chat room usernames
     */
    private Map<String, Boolean> allOtherUsernames;

    /**
     * The chat room manager
     */
    @Inject
    private ChatRoomManager manager;

    /**
     * The user bean
     */
    @Inject
    private UserBean userBean;
    
    /**
     * The user service
     */
    @EJB
    private UserService userService;

    /**
     * This method executes after construction to retrieve the list of names for
     * active rooms and the list of all other usernames.
     */
    @PostConstruct
    public void init() {

        userBean.setSelectedRoom(null);
        rooms = new ArrayList<>();
        rooms.addAll(manager.getActiveChatRooms().keySet());
        allOtherUsernames = new HashMap<>();
        for(ChatRoomUser user: userService.findAllUsers()){
            if(!userBean.getUser().getUsername().equals(user.getUsername())){
                allOtherUsernames.put(user.getUsername(), false);
            }            
        }
    }

    /**
     * This method determines if the user is able to open a chat room and
     * forwards the user appropriately.
     *
     * @return the target page
     */
    public String processHost() {
        
        ArrayList<String> invitees = new ArrayList<>();
        for(String username: allOtherUsernames.keySet()){
            if(allOtherUsernames.get(username)){
                invitees.add(username);
            }
        }        
        if(invitees.isEmpty()){
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage("You must select at least one invitee to open a chat room"));
            return "index";
        }

        ProcessingStatus status = manager.processHost(userBean.getUser(),
                userBean.getSelectedRoom(), invitees);
        if (status.getStatus().equals(ProcessingStatus.ERROR)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(status.getDetails()));
            return "index";
        }
        return "/chat/index.xhtml";
    }

    /**
     * This method determines if the user is able to join a chat room and
     * forwards the user appropriately.
     *
     * @return the target page
     */
    public String processGuest() {

        ProcessingStatus status = manager.processGuest(userBean.getUser(),
                userBean.getSelectedRoom());
        if (status.getStatus().equals(ProcessingStatus.ERROR)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(status.getDetails()));
            return "index";
        }
        return "/chat/index.xhtml";
    }

    /**
     * Get the value of userBean
     *
     * @return the value of userBean
     */
    public UserBean getUserBean() {
        return userBean;
    }

    /**
     * Set the value of userBean
     *
     * @param userBean the value of userBean
     */
    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }

    /**
     * Get the value of rooms
     *
     * @return the value of rooms
     */
    public ArrayList<ChatRoom> getRooms() {
        return rooms;
    }

    /**
     * Set the value of rooms
     *
     * @param rooms new value of rooms
     */
    public void setRooms(ArrayList<ChatRoom> rooms) {
        this.rooms = rooms;
    }

    /**
     * Get the value of allOtherUsernames
     *
     * @return the value of allOtherUsernames
     */
    public Map<String, Boolean> getAllOtherUsernames() {
        return allOtherUsernames;
    }

    /**
     * Set the value of allOtherUsernames
     *
     * @param allOtherUsernames the new value of allOtherUsernames
     */
    public void setAllOtherUsernames(Map<String, Boolean> allOtherUsernames) {
        this.allOtherUsernames = allOtherUsernames;
    }
}
