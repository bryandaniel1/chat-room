package chat.ejb.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

/**
 * This class represents the occurrence of a conversation in a chat room on a
 * certain day.
 *
 * @author Bryan Daniel
 */
public class Conversation implements Serializable {
    
    /**
     * Serial version UID
     */
    private static final long serialVersionUID = -2792505625064806157L;
    
    /**
     * The index of this conversation
     */
    private BigDecimal index;

    /**
     * The day the conversation occurred
     */
    private Date dateOccurred;
    
    /**
     * The name of the room in which the conversation occurred
     */
    private String roomName;

    /**
     * Parameterized constructor
     * 
     * @param index the index
     * @param dateOccurred the day
     * @param roomName the room name
     */
    public Conversation(Double index, Date dateOccurred, String roomName) {
        this.index = new BigDecimal(index);
        this.dateOccurred = dateOccurred;
        this.roomName = roomName;
    }

    /**
     * Get the value of index
     *
     * @return the value of index
     */
    public BigDecimal getIndex() {
        return index;
    }

    /**
     * Set the value of index
     *
     * @param index new value of index
     */
    public void setIndex(BigDecimal index) {
        this.index = index;
    }

    /**
     * Get the value of dateOccurred
     *
     * @return the value of dateOccurred
     */
    public Date getDateOccurred() {
        return dateOccurred;
    }

    /**
     * Set the value of dateOccurred
     *
     * @param dateOccurred new value of dateOccurred
     */
    public void setDateOccurred(Date dateOccurred) {
        this.dateOccurred = dateOccurred;
    }

    /**
     * Get the value of roomName
     *
     * @return the value of roomName
     */
    public String getRoomName() {
        return roomName;
    }

    /**
     * Set the value of roomName
     *
     * @param roomName new value of roomName
     */
    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
}
