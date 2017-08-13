package chat.ejb.entity;

import chat.ejb.model.Conversation;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.codehaus.jackson.annotate.JsonBackReference;

/**
 * This entity class represents a user message delivered in a chat room.
 *
 * @author Bryan Daniel
 */
@Entity
@Table(name = "Message")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Message.findAll", query = "SELECT m FROM Message m"),
    @NamedQuery(name = "Message.findById", query = "SELECT m FROM Message m WHERE m.id = :id"),
    @NamedQuery(name = "Message.findByTimeWritten", query = "SELECT m FROM Message m WHERE m.timeWritten = :timeWritten"),
    @NamedQuery(name = "Message.findByMessage", query = "SELECT m FROM Message m WHERE m.message = :message"),
    @NamedQuery(name = "Message.findByImage", query = "SELECT m FROM Message m WHERE m.image = :image"),
    @NamedQuery(name = "Message.findConversationMessages",
            query = "SELECT m FROM Message m WHERE m.timeWritten >= :timeWritten "
            + "AND m.timeWritten < :dayAfter "
            + "AND m.roomName = :roomName ORDER BY m.timeWritten")})
@SqlResultSetMapping(name = "ConversationMapping", classes = {
    @ConstructorResult(targetClass = Conversation.class, columns = {
        @ColumnResult(name = "conversation_index"),
        @ColumnResult(name = "day"),
        @ColumnResult(name = "room")
    })
})
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "time_written")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeWritten;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1000)
    @Column(name = "message")
    private String message;
    @Basic(optional = false)
    @NotNull
    @Column(name = "image")
    private boolean image;
    @NotNull
    @Column(name = "video")
    private boolean video;
    @JoinColumn(name = "username", referencedColumnName = "username")
    @ManyToOne(optional = false)
    @JsonBackReference
    private ChatRoomUser username;
    @JoinColumn(name = "room_name", referencedColumnName = "room_name")
    @ManyToOne(optional = false)
    @JsonBackReference
    private ChatRoom roomName;

    public Message() {
    }

    public Message(Long id) {
        this.id = id;
    }

    public Message(Long id, Date timeWritten, String message, boolean image) {
        this.id = id;
        this.timeWritten = timeWritten;
        this.message = message;
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getTimeWritten() {
        return timeWritten;
    }

    public void setTimeWritten(Date timeWritten) {
        this.timeWritten = timeWritten;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean getImage() {
        return image;
    }

    public void setImage(boolean image) {
        this.image = image;
    }

    public boolean getVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public ChatRoomUser getUsername() {
        return username;
    }

    public void setUsername(ChatRoomUser username) {
        this.username = username;
    }

    public ChatRoom getRoomName() {
        return roomName;
    }

    public void setRoomName(ChatRoom roomName) {
        this.roomName = roomName;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof Message)) {
            return false;
        }
        Message other = (Message) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "chat.ejb.entity.Message[ id=" + id + " ]";
    }

}
