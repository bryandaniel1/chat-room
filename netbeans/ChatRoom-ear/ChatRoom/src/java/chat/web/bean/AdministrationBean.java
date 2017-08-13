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
import chat.ejb.service.AccountService;
import chat.ejb.service.UserService;
import chat.web.model.EmailMessenger;
import chat.web.model.EmailMessengerFactory;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 * This managed bean supports the operations and UI components of the
 * administration page.
 *
 * @author Bryan Daniel
 */
@Named(value = "administrationBean")
@ViewScoped
public class AdministrationBean implements Serializable {

    /**
     * Serial version UID
     */
    private static final long serialVersionUID = 5999011933042102361L;

    /**
     * The user service
     */
    @EJB(beanName = "userService")
    private UserService userService;

    /**
     * The user service
     */
    @EJB(beanName = "accountService")
    private AccountService accountService;

    /**
     * The list of chat room users
     */
    private List<ChatRoomUser> users;

    /**
     * The message to indicate a successful operation
     */
    private String successMessage;

    /**
     * The email message to send to a user
     */
    private String emailMessage;

    /**
     * This method is executed after construction to populate the list of users.
     */
    @PostConstruct
    public void init() {
        setUsers(userService.findAllUsers());
    }

    /**
     * This method activates the given user and resets the values for the user
     * list.
     *
     * @param userToActivate the user to activate
     */
    public void activateUser(ChatRoomUser userToActivate) {

        setSuccessMessage(null);

        if (accountService.activateAccountByUsername(userToActivate.getUsername())) {
            setUsers(userService.findAllUsers());
            setSuccessMessage(MessageFormat.format("The user, {0}, has been successfully activated.",
                    userToActivate.getUsername()));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(MessageFormat
                    .format("The activation of user, {0}, was not successful. Inspect the logs to find the root cause.",
                            userToActivate.getUsername())));
        }
    }

    /**
     * This method deactivates the given user and resets the values for the user
     * list.
     *
     * @param userToDeactivate the user to deactivate
     */
    public void deactivateUser(ChatRoomUser userToDeactivate) {

        setSuccessMessage(null);

        if (accountService.deactivateAccountByUsername(userToDeactivate.getUsername())) {
            setUsers(userService.findAllUsers());
            setSuccessMessage(MessageFormat.format("The user, {0}, has been successfully deactivated.",
                    userToDeactivate.getUsername()));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(MessageFormat
                    .format("The deactivation of user, {0}, was not successful. Inspect the logs to find the root cause of the failure.",
                            userToDeactivate.getUsername())));
        }
    }

    /**
     * This method sends the administrator's message to the given user via
     * email.
     *
     * @param user the user
     */
    public void sendEmailMessage(ChatRoomUser user) {

        setSuccessMessage(null);
        EmailMessengerFactory emailMessengerfactory = new EmailMessengerFactory();
        EmailMessenger messenger = emailMessengerfactory.produceAdministratorEmailMessenger(user,
                emailMessage);
        if (messenger.sendMail(user.getUserAccount().getEmailAddress())) {
            setSuccessMessage(MessageFormat.format("The email message was successfully sent to {0}.",
                    user.getUsername()));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(MessageFormat
                    .format("The email message for user, {0}, was not successfully sent. Inspect the logs to find the root cause of the failure.",
                            user.getUsername())));
        }
    }

    /**
     * Get the value of users
     *
     * @return the value of users
     */
    public List<ChatRoomUser> getUsers() {
        return users;
    }

    /**
     * Set the value of users
     *
     * @param users new value of users
     */
    public void setUsers(List<ChatRoomUser> users) {
        this.users = users;
    }

    /**
     * Get the value of successMessage
     *
     * @return the value of successMessage
     */
    public String getSuccessMessage() {
        return successMessage;
    }

    /**
     * Set the value of successMessage
     *
     * @param successMessage new value of successMessage
     */
    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }

    /**
     * Get the value of emailMessage
     *
     * @return the value of emailMessage
     */
    public String getEmailMessage() {
        return emailMessage;
    }

    /**
     * Set the value of emailMessage
     *
     * @param emailMessage new value of emailMessage
     */
    public void setEmailMessage(String emailMessage) {
        this.emailMessage = emailMessage;
    }
}
