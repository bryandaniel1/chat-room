package chat.web.model;

import chat.ejb.model.VideoMessenger;
import chat.web.bean.ChatRoomManager;
import chat.web.bean.MessageDispatcher.MediaType;
import javax.servlet.http.Part;

/**
 * This VideoMessenger implementation holds the Part object for a video upload
 * and provides functionality for relaying results of the processing performed
 * by the file-handling service.
 *
 * @author Bryan Daniel
 */
public class ChatVideoMessenger implements VideoMessenger {

    /**
     * The chat room manager
     */
    private final ChatRoomManager manager;

    /**
     * The user's session ID
     */
    private String httpSessionId;

    /**
     * The video to upload
     */
    private Part videoToUpload;

    /**
     * Parameterized constructor
     *
     * @param httpSessionId the user's session ID
     * @param manager the chat room manager
     * @param videoToUpload the video
     */
    public ChatVideoMessenger(ChatRoomManager manager, String httpSessionId, Part videoToUpload) {
        this.manager = manager;
        this.httpSessionId = httpSessionId;
        this.videoToUpload = videoToUpload;
    }

    /**
     * This method sends the video message using the manager.
     *
     * @param videoNumber the video number
     */
    @Override
    public void relayResults(Long videoNumber) {
        manager.sendMediaMessage(httpSessionId, videoNumber, MediaType.VIDEO);
    }

    /**
     * Get the value of httpSessionId
     *
     * @return the value of httpSessionId
     */
    public String getHttpSessionId() {
        return httpSessionId;
    }

    /**
     * Set the value of httpSessionId
     *
     * @param httpSessionId new value of httpSessionId
     */
    public void setHttpSessionId(String httpSessionId) {
        this.httpSessionId = httpSessionId;
    }

    /**
     * Get the value of videoToUpload
     *
     * @return the value of videoToUpload
     */
    @Override
    public Part getVideoToUpload() {
        return videoToUpload;
    }

    /**
     * Set the value of videoToUpload
     *
     * @param videoToUpload new value of videoToUpload
     */
    public void setVideoToUpload(Part videoToUpload) {
        this.videoToUpload = videoToUpload;
    }
}
