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
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * This entity class represents a chat room user.
 *
 * @author Bryan Daniel
 */
@Entity
@Table(name = "ChatRoomUser")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ChatRoomUser.findAll", query = "SELECT c FROM ChatRoomUser c"),
    @NamedQuery(name = "ChatRoomUser.findByUsername", query = "SELECT c FROM ChatRoomUser c WHERE c.username = :username"),
    @NamedQuery(name = "ChatRoomUser.findByFirstName", query = "SELECT c FROM ChatRoomUser c WHERE c.firstName = :firstName"),
    @NamedQuery(name = "ChatRoomUser.findByLastName", query = "SELECT c FROM ChatRoomUser c WHERE c.lastName = :lastName"),
    @NamedQuery(name = "ChatRoomUser.findByUserRole", query = "SELECT c FROM ChatRoomUser c WHERE c.userRole = :userRole"),
    @NamedQuery(name = "ChatRoomUser.findAllOrderByUsername", query = "SELECT c FROM ChatRoomUser c ORDER BY c.username")})
@NamedStoredProcedureQuery(name = "generateUsername", procedureName = "generateUsername",
        parameters = {
            @StoredProcedureParameter(mode = ParameterMode.IN, type = String.class, name = "first_name_in"),
            @StoredProcedureParameter(mode = ParameterMode.OUT, type = String.class, name = "username")
        }
)
public class ChatRoomUser implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "username")
    private String username;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "first_name")
    private String firstName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "last_name")
    private String lastName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "user_role")
    private String userRole;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "username")
    @JsonBackReference
    private List<Message> messageList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "roomCreator")
    @JsonBackReference
    private List<ChatRoom> chatRoomList;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "chatRoomUser")
    @JsonBackReference
    private UserCredentials userCredentials;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "chatRoomUser")
    @JsonBackReference
    private List<UserSignOut> userSignOutList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "chatRoomUser")
    @JsonBackReference
    private List<UserSignIn> userSignInList;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "chatRoomUser")
    @JsonBackReference
    private UserAccount userAccount;

    /**
     * Default constructor
     */
    public ChatRoomUser() {
    }

    /**
     * Parameterized constructor setting username
     *
     * @param username the username
     */
    public ChatRoomUser(String username) {
        this.username = username;
    }

    /**
     * Parameterized constructor setting username, firstName, lastName, and
     * userRole
     *
     * @param username the username
     * @param firstName the user's first name
     * @param lastName the user's last name
     * @param userRole the user role
     */
    public ChatRoomUser(String username, String firstName, String lastName, String userRole) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userRole = userRole;
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
     * Gets the value of firstName
     *
     * @return the value of firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the value of firstName
     *
     * @param firstName the value of firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the value of lastName
     *
     * @return the value of lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the value of lastName
     *
     * @param lastName the value of lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the value of userRole
     *
     * @return the value of userRole
     */
    public String getUserRole() {
        return userRole;
    }

    /**
     * Sets the value of userRole
     *
     * @param userRole the value of userRole
     */
    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    /**
     * Gets the value of messageList
     *
     * @return the value of messageList
     */
    @XmlTransient
    public List<Message> getMessageList() {
        return messageList;
    }

    /**
     * Sets the value of messageList
     *
     * @param messageList the value of messageList
     */
    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }

    /**
     * Gets the value of chatRoomList
     *
     * @return the value of chatRoomList
     */
    @XmlTransient
    public List<ChatRoom> getChatRoomList() {
        return chatRoomList;
    }

    /**
     * Sets the value of chatRoomList
     *
     * @param chatRoomList the value of chatRoomList
     */
    public void setChatRoomList(List<ChatRoom> chatRoomList) {
        this.chatRoomList = chatRoomList;
    }

    /**
     * Gets the value of userCredentials
     *
     * @return the value of userCredentials
     */
    public UserCredentials getUserCredentials() {
        return userCredentials;
    }

    /**
     * Sets the value of userCredentials
     *
     * @param userCredentials the value of userCredentials
     */
    public void setUserCredentials(UserCredentials userCredentials) {
        this.userCredentials = userCredentials;
    }

    /**
     * Gets the value of userSignOutList
     *
     * @return the value of userSignOutList
     */
    @XmlTransient
    @JsonIgnore
    public List<UserSignOut> getUserSignOutList() {
        return userSignOutList;
    }

    /**
     * Sets the value of userSignOutList
     *
     * @param userSignOutList the value of userSignOutList
     */
    public void setUserSignOutList(List<UserSignOut> userSignOutList) {
        this.userSignOutList = userSignOutList;
    }

    /**
     * Gets the value of userSignInList
     *
     * @return the value of userSignInList
     */
    @XmlTransient
    @JsonIgnore
    public List<UserSignIn> getUserSignInList() {
        return userSignInList;
    }

    /**
     * Sets the value of userSignInList
     *
     * @param userSignInList the value of userSignInList
     */
    public void setUserSignInList(List<UserSignIn> userSignInList) {
        this.userSignInList = userSignInList;
    }

    /**
     * Gets the value of userAccount
     *
     * @return the value of userAccount
     */
    public UserAccount getUserAccount() {
        return userAccount;
    }

    /**
     * Sets the value of userAccount
     *
     * @param userAccount the value of userAccount
     */
    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (username != null ? username.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof ChatRoomUser)) {
            return false;
        }
        ChatRoomUser other = (ChatRoomUser) object;
        if ((this.username == null && other.username != null) || (this.username != null && !this.username.equals(other.username))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "chat.ejb.entity.ChatRoomUser[ username=" + username + " ]";
    }
}
