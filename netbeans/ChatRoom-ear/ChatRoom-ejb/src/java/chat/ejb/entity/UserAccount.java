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
 * This entity class represents a user's account.
 *
 * @author Bryan Daniel
 */
@Entity
@Table(name = "UserAccount")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UserAccount.findAll", query = "SELECT u FROM UserAccount u"),
    @NamedQuery(name = "UserAccount.findByUsername", query = "SELECT u FROM UserAccount u WHERE u.username = :username"),
    @NamedQuery(name = "UserAccount.findByEmailAddress", query = "SELECT u FROM UserAccount u WHERE u.emailAddress = :emailAddress"),
    @NamedQuery(name = "UserAccount.findByRegistrationCode", query = "SELECT u FROM UserAccount u WHERE u.registrationCode = :registrationCode"),
    @NamedQuery(name = "UserAccount.findByActivated", query = "SELECT u FROM UserAccount u WHERE u.activated = :activated"),
    @NamedQuery(name = "UserAccount.activateAccount", query = "UPDATE UserAccount u SET u.activated = true WHERE u.registrationCode = :registrationCode"),
    @NamedQuery(name = "UserAccount.activateAccountByUsername", query = "UPDATE UserAccount u SET u.activated = true WHERE u.username = :username"),
    @NamedQuery(name = "UserAccount.deactivateAccountByUsername", query = "UPDATE UserAccount u SET u.activated = false WHERE u.username = :username")})
public class UserAccount implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "username")
    private String username;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255, message = "The email address must be no greater than 255 characters in length.")
    @Column(name = "email_address")
    private String emailAddress;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "registration_code")
    private String registrationCode;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activated")
    private boolean activated;
    @JoinColumn(name = "username", referencedColumnName = "username", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private ChatRoomUser chatRoomUser;

    public UserAccount() {
    }

    public UserAccount(String username) {
        this.username = username;
    }

    public UserAccount(String username, String emailAddress, String registrationCode, boolean activated) {
        this.username = username;
        this.emailAddress = emailAddress;
        this.registrationCode = registrationCode;
        this.activated = activated;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getRegistrationCode() {
        return registrationCode;
    }

    public void setRegistrationCode(String registrationCode) {
        this.registrationCode = registrationCode;
    }

    public boolean getActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
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
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserAccount)) {
            return false;
        }
        UserAccount other = (UserAccount) object;
        if ((this.username == null && other.username != null) || (this.username != null && !this.username.equals(other.username))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "chat.ejb.entity.UserAccount[ username=" + username + " ]";
    }

}
