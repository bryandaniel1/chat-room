package chat.ejb.model;

import javax.servlet.http.Part;

/**
 * This interface provides the function for relaying results of a video upload
 * execution.
 *
 * @author Bryan Daniel
 */
public interface VideoMessenger {

    /**
     * This method sends a message concerning the upload results of the video
     * associated with the given number.
     *
     * @param videoNumber the identifying video number
     */
    public void relayResults(Long videoNumber);
    
    /**
     * Get the video to upload
     *
     * @return the value of videoToUpload
     */
    public Part getVideoToUpload();
}
