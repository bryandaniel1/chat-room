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

    public UserSignOutPK() {
    }

    public UserSignOutPK(String username, Date timeSignedOut) {
        this.username = username;
        this.timeSignedOut = timeSignedOut;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getTimeSignedOut() {
        return timeSignedOut;
    }

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
