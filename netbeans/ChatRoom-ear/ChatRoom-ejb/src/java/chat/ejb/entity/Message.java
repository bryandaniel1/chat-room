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
import org.codehaus.jackson.annotate.JsonManagedReference;

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
    @JsonManagedReference
    private ChatRoomUser username;
    @JoinColumn(name = "room_name", referencedColumnName = "room_name")
    @ManyToOne(optional = false)
    @JsonManagedReference
    private ChatRoom roomName;

    /**
     * Default constructor
     */
    public Message() {
    }

    /**
     * Parameterized constructor setting id
     *
     * @param id the message ID
     */
    public Message(Long id) {
        this.id = id;
    }

    /**
     * Parameterized constructor setting id, timeWritten, message, and image
     *
     * @param id the message ID
     * @param timeWritten the time written
     * @param message the message content
     * @param image the indicator of an image message
     */
    public Message(Long id, Date timeWritten, String message, boolean image) {
        this.id = id;
        this.timeWritten = timeWritten;
        this.message = message;
        this.image = image;
    }

    /**
     * Gets the value of id
     *
     * @return the value of id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the value of id
     *
     * @param id the value of id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the value of timeWritten
     *
     * @return the value of timeWritten
     */
    public Date getTimeWritten() {
        return timeWritten;
    }

    /**
     * Sets the value of timeWritten
     *
     * @param timeWritten the value of timeWritten
     */
    public void setTimeWritten(Date timeWritten) {
        this.timeWritten = timeWritten;
    }

    /**
     * Gets the value of message
     *
     * @return the value of message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the value of message
     *
     * @param message the value of message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Gets the value of image
     *
     * @return the value of image
     */
    public boolean getImage() {
        return image;
    }

    /**
     * Sets the value of image
     *
     * @param image the value of image
     */
    public void setImage(boolean image) {
        this.image = image;
    }

    /**
     * Gets the value of video
     *
     * @return the value of video
     */
    public boolean getVideo() {
        return video;
    }

    /**
     * Sets the value of video
     *
     * @param video the value of video
     */
    public void setVideo(boolean video) {
        this.video = video;
    }

    /**
     * Gets the value of username
     *
     * @return the value of username
     */
    public ChatRoomUser getUsername() {
        return username;
    }

    /**
     * Sets the value of username
     *
     * @param username the value of username
     */
    public void setUsername(ChatRoomUser username) {
        this.username = username;
    }

    /**
     * Gets the value of roomName
     *
     * @return the value of roomName
     */
    public ChatRoom getRoomName() {
        return roomName;
    }

    /**
     * Sets the value of roomName
     *
     * @param roomName the value of roomName
     */
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
