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
package chat.web.bean;

import chat.ejb.model.VideoMessenger;
import chat.ejb.service.FileHandlingService;
import chat.web.bean.MessageDispatcher.MediaType;
import chat.web.model.ChatVideoMessenger;
import chat.web.servlet.ImageServlet;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

/**
 * This managed bean supports the operations and UI components of the chat page.
 *
 * @author Bryan Daniel
 */
@Named("chatBean")
@ViewScoped
public class ChatBean implements Serializable {

    /**
     * Serial version UID
     */
    private static final long serialVersionUID = 1587241777156525298L;

    /**
     * The chat room manager
     */
    @Inject
    private ChatRoomManager manager;

    /**
     * The user bean
     */
    @Inject
    private UserBean userBean;

    /**
     * The image handling service
     */
    @EJB(beanName = "imageHandler")
    private FileHandlingService imageHandler;

    /**
     * The video handling service
     */
    @EJB(beanName = "videoHandler")
    private FileHandlingService videoHandler;

    /**
     * The filename
     */
    private String filename;

    /**
     * The name of the image
     */
    private String imageName;

    /**
     * The name of the video
     */
    private String videoName;

    /**
     * The image data
     */
    private byte[] imageData = null;

    /**
     * The image to upload
     */
    private Part fileToUpload;

    /**
     * The indication of an image ready to preview
     */
    private boolean imageReadyForPreview;

    /**
     * Indicates if the file to upload has passed validation
     */
    private boolean fileValid;

    /**
     * The list of users found in the chat room
     */
    private ArrayList<String> usersFoundInRoom;

    /**
     * This method is executed after construction to find the usernames for
     * users found in the chat room.
     */
    @PostConstruct
    public void init() {
        setUsersFoundInRoom(manager.getUsernamesForRoom(userBean.getSelectedRoom()));
    }

    /**
     * This method calls the appropriate upload method determined by the file
     * content type.
     */
    public void uploadFile() {

        if (!fileValid) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("A valid image file must be selected for upload."));
        } else {
            setFilename(getFileToUpload().getSubmittedFileName());
            if (getFileToUpload().getContentType().startsWith("image/")) {
                uploadImage();
            } else if (getFileToUpload().getContentType().startsWith("video/")) {
                uploadVideo();
            }
        }
    }

    /**
     * This method retrieves the image after validation and saves it to the file
     * system.
     */
    public void uploadImage() {

        Long imageNumber = null;
        setImageName(getFilename().substring(0, this.filename.indexOf(".")));
        HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance()
                .getExternalContext().getSession(false);

        try (InputStream inputStream = getFileToUpload().getInputStream()) {
            imageNumber = imageHandler.saveToFileSystem(userBean.getUser().getUsername(),
                    getImageName(), ImageIO.read(inputStream));
        } catch (IOException ex) {
            Logger.getLogger(ChatBean.class.getName()).log(Level.SEVERE,
                    "An IOException occurred in the uploadImage method.", ex);
        } finally {
            setFileToUpload(null);
            setImageReadyForPreview(false);
            httpSession.setAttribute(ImageServlet.IMAGE_KEY, null);
        }
        if (imageNumber != null) {
            manager.sendMediaMessage(httpSession.getId(), imageNumber, MediaType.IMAGE);
        }
    }

    /**
     * This method creates a video messenger containing the Part object and
     * invokes the saveToFileSystem method of the video handler for processing
     * of the upload. The messenger contains a callback method to propagate the
     * video message.
     */
    public void uploadVideo() {

        setVideoName(getFilename());
        String sessionId = ((HttpSession) FacesContext.getCurrentInstance()
                .getExternalContext().getSession(false)).getId();
        VideoMessenger messenger = new ChatVideoMessenger(manager, sessionId, getFileToUpload());
        videoHandler.saveToFileSystem(userBean.getUser().getUsername(), getVideoName(), messenger);
        setFileToUpload(null);
    }

    /**
     * The method is invoked after validation failure to clear the previous
     * image if one is stored.
     */
    public void checkValidation() {
        if (FacesContext.getCurrentInstance().isValidationFailed()) {
            setFileToUpload(null);
            setFileValid(false);
            setImageReadyForPreview(false);
            ((HttpSession) FacesContext.getCurrentInstance().getExternalContext()
                    .getSession(false)).setAttribute(ImageServlet.IMAGE_KEY, null);
        }
    }

    /**
     * Gets the value of userBean
     *
     * @return the value of user bean
     */
    public UserBean getUserBean() {
        return userBean;
    }

    /**
     * Sets the value of userBean
     *
     * @param userBean the new value of userBean
     */
    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }

    /**
     * Gets the value of filename
     *
     * @return the filename
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Sets the value of filename
     *
     * @param filename the filename
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * Gets the value of imageName
     *
     * @return the image name
     */
    public String getImageName() {
        return imageName;
    }

    /**
     * Sets the value of imageName
     *
     * @param imageName the image name
     */
    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    /**
     * Get the value of videoName
     *
     * @return the value of videoName
     */
    public String getVideoName() {
        return videoName;
    }

    /**
     * Set the value of videoName
     *
     * @param videoName new value of videoName
     */
    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    /**
     * Gets the value of imageData
     *
     * @return the image data
     */
    public byte[] getImageData() {
        return imageData;
    }

    /**
     * Sets the value of imageData
     *
     * @param imageData the image data
     */
    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    /**
     * Get the value of fileToUpload
     *
     * @return the value of fileToUpload
     */
    public Part getFileToUpload() {
        return fileToUpload;
    }

    /**
     * Set the value of fileToUpload and set the boolean to indicate that the
     * image has passed validation
     *
     * @param fileToUpload new value of fileToUpload
     */
    public void setFileToUpload(Part fileToUpload) {
        this.fileToUpload = fileToUpload;
        setFileValid(fileToUpload != null);
        if (getFileValid() && getFileToUpload().getContentType().startsWith("image/")) {
            ((HttpSession) FacesContext.getCurrentInstance().getExternalContext()
                    .getSession(false)).setAttribute(ImageServlet.IMAGE_KEY, fileToUpload);
            setImageReadyForPreview(true);
        }
    }

    /**
     * Get the value of imageReadyForPreview
     *
     * @return the value of imageReadyForPreview
     */
    public boolean getImageReadyForPreview() {
        return imageReadyForPreview;
    }

    /**
     * Set the value of imageReadyForPreview
     *
     * @param imageReadyForPreview new value of imageReadyForPreview
     */
    public void setImageReadyForPreview(boolean imageReadyForPreview) {
        this.imageReadyForPreview = imageReadyForPreview;
    }

    /**
     * Get the value of fileValid
     *
     * @return the value of fileValid
     */
    public boolean getFileValid() {
        return fileValid;
    }

    /**
     * Set the value of fileValid
     *
     * @param fileValid new value of fileValid
     */
    public void setFileValid(boolean fileValid) {
        this.fileValid = fileValid;
    }

    /**
     * Get the value of usersFoundInRoom
     *
     * @return the value of usersFoundInRoom
     */
    public ArrayList<String> getUsersFoundInRoom() {
        return usersFoundInRoom;
    }

    /**
     * Set the value of usersFoundInRoom
     *
     * @param usersFoundInRoom new value of usersFoundInRoom
     */
    public void setUsersFoundInRoom(ArrayList<String> usersFoundInRoom) {
        this.usersFoundInRoom = usersFoundInRoom;
    }
}
