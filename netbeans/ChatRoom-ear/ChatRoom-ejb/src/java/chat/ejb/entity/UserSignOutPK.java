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
 * This entity class represents a user's sign-out-time primary key.
 *
 * @author Bryan Daniel
 */
@Embeddable
public class UserSignOutPK implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "username")
    private String username;
    @Basic(optional = false)
    @NotNull
    @Column(name = "time_signed_out")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeSignedOut;

    /**
     * Default constructor
     */
    public UserSignOutPK() {
    }

    /**
     * Parameterized constructor setting username and timeSignedOut
     *
     * @param username the username
     * @param timeSignedOut the time signed out
     */
    public UserSignOutPK(String username, Date timeSignedOut) {
        this.username = username;
        this.timeSignedOut = timeSignedOut;
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
     * Gets the value of timeSignedOut
     *
     * @return the value of timeSignedOut
     */
    public Date getTimeSignedOut() {
        return timeSignedOut;
    }

    /**
     * Sets the value of timeSignedOut
     *
     * @param timeSignedOut the value of timeSignedOut
     */
    public void setTimeSignedOut(Date timeSignedOut) {
        this.timeSignedOut = timeSignedOut;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (username != null ? username.hashCode() : 0);
        hash += (timeSignedOut != null ? timeSignedOut.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof UserSignOutPK)) {
            return false;
        }
        UserSignOutPK other = (UserSignOutPK) object;
        if ((this.username == null && other.username != null) || (this.username != null && !this.username.equals(other.username))) {
            return false;
        }
        if ((this.timeSignedOut == null && other.timeSignedOut != null) || (this.timeSignedOut != null && !this.timeSignedOut.equals(other.timeSignedOut))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "chat.ejb.entity.UserSignOutPK[ username=" + username + ", timeSignedOut=" + timeSignedOut + " ]";
    }

}
