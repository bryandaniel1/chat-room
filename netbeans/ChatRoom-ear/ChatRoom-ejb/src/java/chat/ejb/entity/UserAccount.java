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
package chat.ejb.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.codehaus.jackson.annotate.JsonManagedReference;

/**
 * This entity class represents a user's account.
 *
 * @author Bryan Daniel
 */
@Entity
@Table(name = "UserAccount")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UserAccount.findAll", query = "SELECT u FROM UserAccount u"),
    @NamedQuery(name = "UserAccount.findByUsername", query = "SELECT u FROM UserAccount u WHERE u.username = :username"),
    @NamedQuery(name = "UserAccount.findByEmailAddress", query = "SELECT u FROM UserAccount u WHERE u.emailAddress = :emailAddress"),
    @NamedQuery(name = "UserAccount.findByRegistrationCode", query = "SELECT u FROM UserAccount u WHERE u.registrationCode = :registrationCode"),
    @NamedQuery(name = "UserAccount.findByActivated", query = "SELECT u FROM UserAccount u WHERE u.activated = :activated"),
    @NamedQuery(name = "UserAccount.activateAccount", query = "UPDATE UserAccount u SET u.activated = true WHERE u.registrationCode = :registrationCode"),
    @NamedQuery(name = "UserAccount.activateAccountByUsername", query = "UPDATE UserAccount u SET u.activated = true WHERE u.username = :username"),
    @NamedQuery(name = "UserAccount.deactivateAccountByUsername", query = "UPDATE UserAccount u SET u.activated = false WHERE u.username = :username")})
public class UserAccount implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "username")
    private String username;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255, message = "The email address must be no greater than 255 characters in length.")
    @Column(name = "email_address")
    private String emailAddress;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "registration_code")
    private String registrationCode;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activated")
    private boolean activated;
    @JoinColumn(name = "username", referencedColumnName = "username", insertable = false, updatable = false)
    @OneToOne(optional = false)
    @JsonManagedReference
    private ChatRoomUser chatRoomUser;

    /**
     * Default constructor
     */
    public UserAccount() {
    }

    /**
     * Parameterized constructor setting username
     *
     * @param username the username
     */
    public UserAccount(String username) {
        this.username = username;
    }

    /**
     * Parameterized constructor setting username, emailAddress,
     * registrationCode, and activated
     *
     * @param username the username
     * @param emailAddress the email address
     * @param registrationCode the registration code
     * @param activated the indicator of account activation
     */
    public UserAccount(String username, String emailAddress, String registrationCode, boolean activated) {
        this.username = username;
        this.emailAddress = emailAddress;
        this.registrationCode = registrationCode;
        this.activated = activated;
    }

    /**
     * Gets the value of username
     *
     * @return the value of username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the value of username
     *
     * @param username the value of username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the value of emailAddress
     *
     * @return the value of emailAddress
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * Sets the value of emailAddress
     *
     * @param emailAddress the value of emailAddress
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    /**
     * Gets the value of registrationCode
     *
     * @return the value of registrationCode
     */
    public String getRegistrationCode() {
        return registrationCode;
    }

    /**
     * Sets the value of registrationCode
     *
     * @param registrationCode the value of registrationCode
     */
    public void setRegistrationCode(String registrationCode) {
        this.registrationCode = registrationCode;
    }

    /**
     * Gets the value of activated
     *
     * @return the value of activated
     */
    public boolean getActivated() {
        return activated;
    }

    /**
     * Sets the value of activated
     *
     * @param activated the value of activated
     */
    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    /**
     * Gets the value of chatRoomUser
     *
     * @return the value of chatRoomUser
     */
    public ChatRoomUser getChatRoomUser() {
        return chatRoomUser;
    }

    /**
     * Sets the value of chatRoomUser
     *
     * @param chatRoomUser the value of chatRoomUser
     */
    public void setChatRoomUser(ChatRoomUser chatRoomUser) {
        this.chatRoomUser = chatRoomUser;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (username != null ? username.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserAccount)) {
            return false;
        }
        UserAccount other = (UserAccount) object;
        if ((this.username == null && other.username != null) || (this.username != null && !this.username.equals(other.username))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "chat.ejb.entity.UserAccount[ username=" + username + " ]";
    }

}
