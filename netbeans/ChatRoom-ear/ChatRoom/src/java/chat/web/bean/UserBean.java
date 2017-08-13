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

import chat.ejb.entity.ChatRoomUser;
import chat.ejb.entity.UserCredentials;
import chat.ejb.service.AccountService;
import chat.ejb.service.UserService;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * This managed bean handles the chat room user data to support the faces pages.
 *
 * @author Bryan Daniel
 */
@Named("userBean")
@SessionScoped
public class UserBean implements Serializable {

    /**
     * Serial version UID
     */
    private static final long serialVersionUID = 2066501969950286844L;

    /**
     * The user service
     */
    @EJB(beanName = "userService")
    private UserService userService;

    /**
     * The account service
     */
    @EJB(beanName = "accountService")
    private AccountService accountService;

    /**
     * The chat room manager
     */
    @Inject
    private ChatRoomManager manager;

    /**
     * The chat room user
     */
    private ChatRoomUser user;

    /**
     * The name of the room selected by the user
     */
    private String selectedRoom;

    /**
     * The role of the chat user
     */
    private String chatRole;

    /**
     * This method is executed after construction to prepare a new ChatRoomUser
     * object.
     */
    @PostConstruct
    public void init() {
        user = new ChatRoomUser();
        user.setUserCredentials(new UserCredentials());
    }

    /**
     * This method authenticates the user, records the sign-in time, stores the
     * username in the session, logs the user's new session ID, and returns the
     * appropriate target.
     *
     * @return the target page
     */
    public String authenticateUser() {

        boolean authenticated = userService.authenticateUser(user);
        if (authenticated) {
            user = userService.findUserByUsername(user.getUserCredentials().getUsername());

            FacesContext facesContext = FacesContext.getCurrentInstance();
            if (!accountService.findActivationStatusByUsername(user.getUsername())) {
                facesContext.addMessage(null, new FacesMessage("Your account must be activated "
                        + "before signing in. If you have registered, an email message was sent "
                        + "to your email address with instructions for account activation."));
                return "failed";
            }
            userService.recordSignIn(user.getUsername());

            HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
            request.changeSessionId();
            HttpSession httpSession = (HttpSession) facesContext.getExternalContext().getSession(true);
            httpSession.setAttribute("userName", user.getUsername());

            Logger.getLogger(UserBean.class.getName()).log(Level.INFO,
                    "User, {0}, has signed in with HTTP session ID, {1}.", new Object[]{user.getUsername(),
                        httpSession.getId()});

            manager.enterChatRoomLobby(httpSession.getId(), user);
            return "/lobby/index.xhtml?faces-redirect=true";
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Sign in failed."));
            return "failed";
        }
    }

    /**
     * This method signs out the chat room user.
     *
     * @return the target page
     */
    public String signOutUser() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession httpSession = (HttpSession) facesContext.getExternalContext().getSession(false);
        if (httpSession != null) {
            manager.exitChatApplication(httpSession.getId());
            httpSession.invalidate();
        }
        return "/index.xhtml?faces-redirect=true";
    }

    /**
     * Gets the value of user
     *
     * @return the user
     */
    public ChatRoomUser getUser() {
        return user;
    }

    /**
     * Sets the value of user
     *
     * @param user the user
     */
    public void setUser(ChatRoomUser user) {
        this.user = user;
    }

    /**
     * Get the value of selectedRoom
     *
     * @return the value of selectedRoom
     */
    public String getSelectedRoom() {
        return selectedRoom;
    }

    /**
     * Set the value of selectedRoom
     *
     * @param selectedRoom new value of selectedRoom
     */
    public void setSelectedRoom(String selectedRoom) {
        this.selectedRoom = selectedRoom;
    }

    /**
     * Get the value of chatRole
     *
     * @return the value of chatRole
     */
    public String getChatRole() {
        return chatRole;
    }

    /**
     * Set the value of chatRole
     *
     * @param chatRole new value of chatRole
     */
    public void setChatRole(String chatRole) {
        this.chatRole = chatRole;
    }
}
