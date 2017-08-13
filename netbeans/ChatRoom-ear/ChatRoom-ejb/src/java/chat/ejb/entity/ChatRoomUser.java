package chat.ejb.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonManagedReference;

/**
 * This entity class represents a chat room user.
 *
 * @author Bryan Daniel
 */
@Entity
@Table(name = "ChatRoomUser")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ChatRoomUser.findAll", query = "SELECT c FROM ChatRoomUser c"),
    @NamedQuery(name = "ChatRoomUser.findByUsername", query = "SELECT c FROM ChatRoomUser c WHERE c.username = :username"),
    @NamedQuery(name = "ChatRoomUser.findByFirstName", query = "SELECT c FROM ChatRoomUser c WHERE c.firstName = :firstName"),
    @NamedQuery(name = "ChatRoomUser.findByLastName", query = "SELECT c FROM ChatRoomUser c WHERE c.lastName = :lastName"),
    @NamedQuery(name = "ChatRoomUser.findByUserRole", query = "SELECT c FROM ChatRoomUser c WHERE c.userRole = :userRole"),
    @NamedQuery(name = "ChatRoomUser.findAllOrderByUsername", query = "SELECT c FROM ChatRoomUser c ORDER BY c.username")})
@NamedStoredProcedureQuery(name = "generateUsername", procedureName = "generateUsername", 
        parameters = {
            @StoredProcedureParameter(mode = ParameterMode.IN, type = String.class, name = "first_name_in"), 
            @StoredProcedureParameter(mode = ParameterMode.OUT, type = String.class, name = "username")
        }
)
public class ChatRoomUser implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "username")
    private String username;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "first_name")
    private String firstName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "last_name")
    private String lastName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "user_role")
    private String userRole;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "username")
    @JsonManagedReference
    private List<Message> messageList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "roomCreator")
    private List<ChatRoom> chatRoomList;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "chatRoomUser")
    private UserCredentials userCredentials;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "chatRoomUser")
    private List<UserSignOut> userSignOutList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "chatRoomUser")
    private List<UserSignIn> userSignInList;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "chatRoomUser")
    private UserAccount userAccount;

    public ChatRoomUser() {
    }

    public ChatRoomUser(String username) {
        this.username = username;
    }

    public ChatRoomUser(String username, String firstName, String lastName, String userRole) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userRole = userRole;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    @XmlTransient
    public List<Message> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }

    @XmlTransient
    public List<ChatRoom> getChatRoomList() {
        return chatRoomList;
    }

    public void setChatRoomList(List<ChatRoom> chatRoomList) {
        this.chatRoomList = chatRoomList;
    }

    public UserCredentials getUserCredentials() {
        return userCredentials;
    }

    public void setUserCredentials(UserCredentials userCredentials) {
        this.userCredentials = userCredentials;
    }

    @XmlTransient
    @JsonIgnore
    public List<UserSignOut> getUserSignOutList() {
        return userSignOutList;
    }

    public void setUserSignOutList(List<UserSignOut> userSignOutList) {
        this.userSignOutList = userSignOutList;
    }

    @XmlTransient
    @JsonIgnore
    public List<UserSignIn> getUserSignInList() {
        return userSignInList;
    }

    public void setUserSignInList(List<UserSignIn> userSignInList) {
        this.userSignInList = userSignInList;
    }

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (username != null ? username.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof ChatRoomUser)) {
            return false;
        }
        ChatRoomUser other = (ChatRoomUser) object;
        if ((this.username == null && other.username != null) || (this.username != null && !this.username.equals(other.username))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "chat.ejb.entity.ChatRoomUser[ username=" + username + " ]";
    }
}
