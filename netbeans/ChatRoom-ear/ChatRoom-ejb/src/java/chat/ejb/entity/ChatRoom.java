package chat.ejb.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.codehaus.jackson.annotate.JsonManagedReference;

/**
 * This entity class represents a chat room.
 *
 * @author Bryan Daniel
 */
@Entity
@Table(name = "ChatRoom")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ChatRoom.findAll", query = "SELECT c FROM ChatRoom c"),
    @NamedQuery(name = "ChatRoom.findByRoomName", query = "SELECT c FROM ChatRoom c WHERE c.roomName = :roomName"),
    @NamedQuery(name = "ChatRoom.findByTimeCreated", query = "SELECT c FROM ChatRoom c WHERE c.timeCreated = :timeCreated")})
public class ChatRoom implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "room_name")
    private String roomName;
    @Basic(optional = false)
    @NotNull
    @Column(name = "time_created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeCreated;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "roomName")
    @JsonManagedReference
    private List<Message> messageList;
    @JoinColumn(name = "room_creator", referencedColumnName = "username")
    @ManyToOne(optional = false)
    private ChatRoomUser roomCreator;

    public ChatRoom() {
    }

    public ChatRoom(String roomName) {
        this.roomName = roomName;
    }

    public ChatRoom(String roomName, Date timeCreated) {
        this.roomName = roomName;
        this.timeCreated = timeCreated;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public Date getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(Date timeCreated) {
        this.timeCreated = timeCreated;
    }

    @XmlTransient
    public List<Message> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }

    public ChatRoomUser getRoomCreator() {
        return roomCreator;
    }

    public void setRoomCreator(ChatRoomUser roomCreator) {
        this.roomCreator = roomCreator;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roomName != null ? roomName.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof ChatRoom)) {
            return false;
        }
        ChatRoom other = (ChatRoom) object;
        if ((this.roomName == null && other.roomName != null) || (this.roomName != null && !this.roomName.equals(other.roomName))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "chat.ejb.entity.ChatRoom[ roomName=" + roomName + " ]";
    }

}
