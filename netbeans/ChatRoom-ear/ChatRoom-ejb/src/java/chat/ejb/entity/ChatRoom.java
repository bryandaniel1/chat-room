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
import org.codehaus.jackson.annotate.JsonBackReference;
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
    @JsonBackReference
    private List<Message> messageList;
    @JoinColumn(name = "room_creator", referencedColumnName = "username")
    @ManyToOne(optional = false)
    @JsonManagedReference
    private ChatRoomUser roomCreator;

    /**
     * Default constructor
     */
    public ChatRoom() {
    }

    /**
     * Parameterized constructor setting roomName
     *
     * @param roomName the room name
     */
    public ChatRoom(String roomName) {
        this.roomName = roomName;
    }

    /**
     * Parameterized constructor setting roomName and timeCreated
     *
     * @param roomName the room name
     * @param timeCreated the time created
     */
    public ChatRoom(String roomName, Date timeCreated) {
        this.roomName = roomName;
        this.timeCreated = timeCreated;
    }

    /**
     * Gets the value of roomName
     *
     * @return the value of roomName
     */
    public String getRoomName() {
        return roomName;
    }

    /**
     * Sets the value of roomName
     *
     * @param roomName the value of roomName
     */
    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    /**
     * Gets the value of timeCreated
     *
     * @return the value of timeCreated
     */
    public Date getTimeCreated() {
        return timeCreated;
    }

    /**
     * Sets the value of timeCreated
     *
     * @param timeCreated the value of timeCreated
     */
    public void setTimeCreated(Date timeCreated) {
        this.timeCreated = timeCreated;
    }

    /**
     * Gets the value of messageList
     *
     * @return the value of messageList
     */
    @XmlTransient
    public List<Message> getMessageList() {
        return messageList;
    }

    /**
     * Sets the value of messageList
     *
     * @param messageList the value of messageList
     */
    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }

    /**
     * Gets the value of roomCreator
     *
     * @return the value of roomCreator
     */
    public ChatRoomUser getRoomCreator() {
        return roomCreator;
    }

    /**
     * Sets the value of roomCreator
     *
     * @param roomCreator the value of roomCreator
     */
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
