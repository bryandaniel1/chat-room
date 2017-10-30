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
 * This entity class stores a user's credentials.
 *
 * @author Bryan Daniel
 */
@Entity
@Table(name = "UserCredentials")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UserCredentials.findAll", query = "SELECT u FROM UserCredentials u"),
    @NamedQuery(name = "UserCredentials.findByUsername", query = "SELECT u FROM UserCredentials u WHERE u.username = :username"),
    @NamedQuery(name = "UserCredentials.findByPassword", query = "SELECT u FROM UserCredentials u WHERE u.password = :password")})
public class UserCredentials implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "username")
    private String username;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 60)
    @Column(name = "password")
    private String password;
    @JoinColumn(name = "username", referencedColumnName = "username", insertable = false, updatable = false)
    @OneToOne(optional = false)
    @JsonManagedReference
    private ChatRoomUser chatRoomUser;

    /**
     * Default constructor
     */
    public UserCredentials() {
    }

    /**
     * Parameterized constructor setting username
     *
     * @param username the username
     */
    public UserCredentials(String username) {
        this.username = username;
    }

    /**
     * Parameterized constructor setting username and password
     *
     * @param username the username
     * @param password the password
     */
    public UserCredentials(String username, String password) {
        this.username = username;
        this.password = password;
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
     * Gets the value of password
     *
     * @return the value of password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the value of password
     *
     * @param password the value of password
     */
    public void setPassword(String password) {
        this.password = password;
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

        if (!(object instanceof UserCredentials)) {
            return false;
        }
        UserCredentials other = (UserCredentials) object;
        if ((this.username == null && other.username != null) || (this.username != null && !this.username.equals(other.username))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "chat.ejb.entity.UserCredentials[ username=" + username + " ]";
    }

}
