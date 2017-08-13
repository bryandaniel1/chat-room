package chat.web.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.Part;

/**
 * This Validator implementation ensures that the fileToUpload to upload is an
 * image fileToUpload.
 *
 * @author Bryan Daniel
 */
@FacesValidator("fileValidator")
public class FileValidator implements Validator {

    /**
     * This method validates that the file to upload exists and the content type
     * of the file is image or video.
     *
     * @param context the faces context
     * @param component the UI component
     * @param value the fileToUpload to upload
     * @throws ValidatorException the exception thrown if the fileToUpload is
     * not an image or video
     */
    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        
        Part fileToUpload = (Part) value;

        if (fileToUpload == null || fileToUpload.getSize() == 0) {
            throw new ValidatorException(new FacesMessage("The file selected for upload is empty."));
        }

        if (!fileToUpload.getContentType().startsWith("video/") 
                && !fileToUpload.getContentType().startsWith("image/")) {
            throw new ValidatorException(new FacesMessage("The file selected for upload is not an image or video."));
        }
    }
}
