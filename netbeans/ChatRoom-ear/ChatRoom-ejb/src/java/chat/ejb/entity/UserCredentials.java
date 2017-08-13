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
    private ChatRoomUser chatRoomUser;

    public UserCredentials() {
    }

    public UserCredentials(String username) {
        this.username = username;
    }

    public UserCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
