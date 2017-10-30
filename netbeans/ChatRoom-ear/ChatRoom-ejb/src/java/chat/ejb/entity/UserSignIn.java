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
import java.util.Date;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import org.codehaus.jackson.annotate.JsonManagedReference;

/**
 * This entity class holds a user's sign-in time.
 *
 * @author Bryan Daniel
 */
@Entity
@Table(name = "UserSignIn")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UserSignIn.findAll", query = "SELECT u FROM UserSignIn u"),
    @NamedQuery(name = "UserSignIn.findByUsername", query = "SELECT u FROM UserSignIn u WHERE u.userSignInPK.username = :username"),
    @NamedQuery(name = "UserSignIn.findByTimeSignedIn", query = "SELECT u FROM UserSignIn u WHERE u.userSignInPK.timeSignedIn = :timeSignedIn"),
    @NamedQuery(name = "UserSignIn.findLastSignIn", query = "SELECT u1.userSignInPK.timeSignedIn FROM UserSignIn u1 WHERE u1.userSignInPK.timeSignedIn = ("
            + "SELECT MAX(u2.userSignInPK.timeSignedIn) FROM UserSignIn u2 WHERE u2.userSignInPK.username = u1.userSignInPK.username "
            + "AND u2.userSignInPK.username = :username)")})
public class UserSignIn implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    protected UserSignInPK userSignInPK;
    @JoinColumn(name = "username", referencedColumnName = "username", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    @JsonManagedReference
    private ChatRoomUser chatRoomUser;

    /**
     * Default constructor
     */
    public UserSignIn() {
    }

    /**
     * Parameterized constructor setting userSignInPK
     *
     * @param userSignInPK the user-sign-in primary key
     */
    public UserSignIn(UserSignInPK userSignInPK) {
        this.userSignInPK = userSignInPK;
    }

    /**
     * Parameterized constructor setting username and timeSignedIn
     *
     * @param username the username
     * @param timeSignedIn the time signed in
     */
    public UserSignIn(String username, Date timeSignedIn) {
        this.userSignInPK = new UserSignInPK(username, timeSignedIn);
    }

    /**
     * Gets the value of userSignInPK
     *
     * @return the value of userSignInPK
     */
    public UserSignInPK getUserSignInPK() {
        return userSignInPK;
    }

    /**
     * Sets the value of userSignInPK
     *
     * @param userSignInPK the value of userSignInPK
     */
    public void setUserSignInPK(UserSignInPK userSignInPK) {
        this.userSignInPK = userSignInPK;
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
        hash += (userSignInPK != null ? userSignInPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof UserSignIn)) {
            return false;
        }
        UserSignIn other = (UserSignIn) object;
        if ((this.userSignInPK == null && other.userSignInPK != null) || (this.userSignInPK != null && !this.userSignInPK.equals(other.userSignInPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "chat.ejb.entity.UserSignIn[ userSignInPK=" + userSignInPK + " ]";
    }

}
