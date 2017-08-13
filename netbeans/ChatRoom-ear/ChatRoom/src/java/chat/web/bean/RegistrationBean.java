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
import chat.ejb.entity.UserAccount;
import chat.ejb.entity.UserCredentials;
import chat.ejb.service.AccountService;
import chat.ejb.service.UserService;
import chat.ejb.util.EncryptionUtil;
import chat.web.model.EmailMessenger;
import chat.web.model.EmailMessengerFactory;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.mindrot.jbcrypt.BCrypt;

/**
 * This managed bean supports the components and operations of the registration
 * page.
 *
 * @author Bryan Daniel
 */
@Named(value = "registrationBean")
@ViewScoped
public class RegistrationBean implements Serializable {

    /**
     * The default user role
     */
    private static final String DEFAULT_ROLE = "user";

    /**
     * The number of log rounds for hashing
     */
    private static final int LOG_ROUNDS = 12;

    /**
     * Serial version UID
     */
    private static final long serialVersionUID = -3957277198000728706L;

    /**
     * The account service
     */
    @EJB(beanName = "accountService")
    private AccountService accountService;

    /**
     * The user service
     */
    @EJB(beanName = "userService")
    private UserService userService;

    /**
     * The new user
     */
    private ChatRoomUser newUser;

    /**
     * The new user credentials
     */
    private UserCredentials newCredentials;

    /**
     * The new user account
     */
    private UserAccount newAccount;

    /**
     * The second password entry for confirmation
     */
    private String confirmPassword;

    /**
     * This method is performed after construction.
     */
    @PostConstruct
    public void init() {
        newUser = new ChatRoomUser();
        newCredentials = new UserCredentials();
        newAccount = new UserAccount();
        newCredentials.setChatRoomUser(newUser);
        newUser.setUserCredentials(newCredentials);
        newAccount.setChatRoomUser(newUser);
        newUser.setUserAccount(newAccount);
    }

    /**
     * This method uses the user service and the account service to create a new
     * account and user in the database. After successful creation, an email
     * messenger is used to send instructions to the new user.
     *
     * @return the destination page
     */
    public String registerNewUser() {

        boolean registrationSuccess = false;
        FacesContext context = FacesContext.getCurrentInstance();

        newUser.setUserRole(DEFAULT_ROLE);
        newUser.setUsername(accountService.generateUsername(newUser.getFirstName()));
        newCredentials.setUsername(newUser.getUsername());
        newCredentials.setPassword(BCrypt.hashpw(newCredentials.getPassword(),
                BCrypt.gensalt(LOG_ROUNDS)));
        newAccount.setUsername(newUser.getUsername());
        newAccount.setActivated(false);
        
        try {
            newAccount.setRegistrationCode(EncryptionUtil.simpleHash(newAccount.getEmailAddress()));
            registrationSuccess = userService.persist(newUser);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(RegistrationBean.class.getName()).log(Level.SEVERE,
                    "A NoSuchAlgorithmException occurred when trying to hash an email address.", ex);
        }

        if (registrationSuccess) {
            EmailMessengerFactory messengerFactory = new EmailMessengerFactory();
            EmailMessenger messenger = messengerFactory
                    .produceRegistrationEmailMessenger(newUser, newAccount.getRegistrationCode());
            boolean emailSent = messenger.sendMail(newAccount.getEmailAddress());
            if (!emailSent) {

                // email failed - adding notification to flash scope to survive redirect                
                context.addMessage(null, new FacesMessage(
                        "An error occurred with email message generation."));
                context.getExternalContext().getFlash().setKeepMessages(true);
            }
            return "success.xhtml?faces-redirect=true";
        }
        context.addMessage(null, new FacesMessage("The registration process failed."));
        return null;
    }

    /**
     * Get the value of newUser
     *
     * @return the value of newUser
     */
    public ChatRoomUser getNewUser() {
        return newUser;
    }

    /**
     * Set the value of newUser
     *
     * @param newUser new value of newUser
     */
    public void setNewUser(ChatRoomUser newUser) {
        this.newUser = newUser;
    }

    /**
     * Get the value of newCredentials
     *
     * @return the value of newCredentials
     */
    public UserCredentials getNewCredentials() {
        return newCredentials;
    }

    /**
     * Set the value of newCredentials
     *
     * @param newCredentials new value of newCredentials
     */
    public void setNewCredentials(UserCredentials newCredentials) {
        this.newCredentials = newCredentials;
    }

    /**
     * Get the value of newAccount
     *
     * @return the value of newAccount
     */
    public UserAccount getNewAccount() {
        return newAccount;
    }

    /**
     * Set the value of newAccount
     *
     * @param newAccount new value of newAccount
     */
    public void setNewAccount(UserAccount newAccount) {
        this.newAccount = newAccount;
    }

    /**
     * Get the value of confirmPassword
     *
     * @return the value of confirmPassword
     */
    public String getConfirmPassword() {
        return confirmPassword;
    }

    /**
     * Set the value of confirmPassword
     *
     * @param confirmPassword new value of confirmPassword
     */
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
