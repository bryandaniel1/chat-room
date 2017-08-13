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
import chat.web.model.ProcessingStatus;
import java.io.Serializable;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
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
     * The list of chat rooms
     */
    private ArrayList<ChatRoom> rooms;

    /**
     * This method executes after construction to retrieve the list of names for
     * active rooms.
     */
    @PostConstruct
    public void init() {

        userBean.setSelectedRoom(null);
        rooms = new ArrayList<>();
        rooms.addAll(manager.getActiveChatRooms().keySet());
    }

    /**
     * This method determines if the user is able to open a chat room and
     * forwards the user appropriately.
     *
     * @return the target page
     */
    public String processHost() {

        ProcessingStatus status = manager.processHost(userBean.getUser(),
                userBean.getSelectedRoom());
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
}
