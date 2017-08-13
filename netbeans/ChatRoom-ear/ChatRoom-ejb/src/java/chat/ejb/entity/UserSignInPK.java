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

    public UserSignInPK() {
    }

    public UserSignInPK(String username, Date timeSignedIn) {
        this.username = username;
        this.timeSignedIn = timeSignedIn;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getTimeSignedIn() {
        return timeSignedIn;
    }

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
