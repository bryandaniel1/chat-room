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
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * This entity class represents a user's sign-in-time primary key.
 *
 * @author Bryan Daniel
 */
@Embeddable
public class UserSignInPK implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "username")
    private String username;
    @Basic(optional = false)
    @NotNull
    @Column(name = "time_signed_in")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeSignedIn;

    /**
     * Default constructor
     */
    public UserSignInPK() {
    }

    /**
     * Parameterized constructor setting username and timeSignedIn
     *
     * @param username the username
     * @param timeSignedIn the time signed in
     */
    public UserSignInPK(String username, Date timeSignedIn) {
        this.username = username;
        this.timeSignedIn = timeSignedIn;
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
     * Gets the value of timeSignedIn
     *
     * @return the value of timeSignedIn
     */
    public Date getTimeSignedIn() {
        return timeSignedIn;
    }

    /**
     * Sets the value of timeSignedIn
     *
     * @param timeSignedIn the value of timeSignedIn
     */
    public void setTimeSignedIn(Date timeSignedIn) {
        this.timeSignedIn = timeSignedIn;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (username != null ? username.hashCode() : 0);
        hash += (timeSignedIn != null ? timeSignedIn.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof UserSignInPK)) {
            return false;
        }
        UserSignInPK other = (UserSignInPK) object;
        if ((this.username == null && other.username != null) || (this.username != null && !this.username.equals(other.username))) {
            return false;
        }
        if ((this.timeSignedIn == null && other.timeSignedIn != null) || (this.timeSignedIn != null && !this.timeSignedIn.equals(other.timeSignedIn))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "chat.ejb.entity.UserSignInPK[ username=" + username + ", timeSignedIn=" + timeSignedIn + " ]";
    }

}
