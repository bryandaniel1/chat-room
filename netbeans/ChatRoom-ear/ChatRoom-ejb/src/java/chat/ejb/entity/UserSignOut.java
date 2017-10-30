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
 * This entity class holds a user's sign-out time.
 *
 * @author Bryan Daniel
 */
@Entity
@Table(name = "UserSignOut")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UserSignOut.findAll", query = "SELECT u FROM UserSignOut u"),
    @NamedQuery(name = "UserSignOut.findByUsername", query = "SELECT u FROM UserSignOut u WHERE u.userSignOutPK.username = :username"),
    @NamedQuery(name = "UserSignOut.findByTimeSignedOut", query = "SELECT u FROM UserSignOut u WHERE u.userSignOutPK.timeSignedOut = :timeSignedOut"),
    @NamedQuery(name = "UserSignIn.findLastSignOut", query = "SELECT u1.userSignOutPK.timeSignedOut FROM UserSignOut u1 WHERE u1.userSignOutPK.timeSignedOut = ("
            + "SELECT MAX(u2.userSignOutPK.timeSignedOut) FROM UserSignOut u2 WHERE u2.userSignOutPK.username = u1.userSignOutPK.username "
            + "AND u2.userSignOutPK.username = :username)")})
public class UserSignOut implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    protected UserSignOutPK userSignOutPK;
    @JoinColumn(name = "username", referencedColumnName = "username", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    @JsonManagedReference
    private ChatRoomUser chatRoomUser;

    /**
     * Default constructor
     */
    public UserSignOut() {
    }

    /**
     * Parameterized constructor setting userSignOutPK
     *
     * @param userSignOutPK the user-sign-out primary key
     */
    public UserSignOut(UserSignOutPK userSignOutPK) {
        this.userSignOutPK = userSignOutPK;
    }

    /**
     * Parameterized constructor setting username and timeSignedOut
     *
     * @param username the username
     * @param timeSignedOut the time signed out
     */
    public UserSignOut(String username, Date timeSignedOut) {
        this.userSignOutPK = new UserSignOutPK(username, timeSignedOut);
    }

    /**
     * Gets the value of userSignOutPK
     *
     * @return the value of userSignOutPK
     */
    public UserSignOutPK getUserSignOutPK() {
        return userSignOutPK;
    }

    /**
     * Sets the value of userSignOutPK
     *
     * @param userSignOutPK the value of userSignOutPK
     */
    public void setUserSignOutPK(UserSignOutPK userSignOutPK) {
        this.userSignOutPK = userSignOutPK;
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
        hash += (userSignOutPK != null ? userSignOutPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof UserSignOut)) {
            return false;
        }
        UserSignOut other = (UserSignOut) object;
        if ((this.userSignOutPK == null && other.userSignOutPK != null) || (this.userSignOutPK != null && !this.userSignOutPK.equals(other.userSignOutPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "chat.ejb.entity.UserSignOut[ userSignOutPK=" + userSignOutPK + " ]";
    }

}
