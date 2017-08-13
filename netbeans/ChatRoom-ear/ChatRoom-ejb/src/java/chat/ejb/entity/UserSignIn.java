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
    private ChatRoomUser chatRoomUser;

    public UserSignIn() {
    }

    public UserSignIn(UserSignInPK userSignInPK) {
        this.userSignInPK = userSignInPK;
    }

    public UserSignIn(String username, Date timeSignedIn) {
        this.userSignInPK = new UserSignInPK(username, timeSignedIn);
    }

    public UserSignInPK getUserSignInPK() {
        return userSignInPK;
    }

    public void setUserSignInPK(UserSignInPK userSignInPK) {
        this.userSignInPK = userSignInPK;
    }

    public ChatRoomUser getChatRoomUser() {
        return chatRoomUser;
    }

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
