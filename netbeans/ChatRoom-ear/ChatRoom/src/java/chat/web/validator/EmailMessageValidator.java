package chat.web.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 * This FacesValidator tests email message input to ensure non-white space
 * content is given.
 *
 * @author Bryan Daniel
 */
@FacesValidator("emailMessageValidator")
public class EmailMessageValidator implements Validator {

    /**
     * This method validates that the value for the email message contains
     * characters other than white space.
     *
     * @param context the FacesContext object
     * @param component the UIComponent object
     * @param value the email message
     * @throws ValidatorException
     */
    @Override
    public void validate(FacesContext context, UIComponent component, Object value) 
            throws ValidatorException {

        if (value != null && ((String) value).trim().length() == 0) {
            throw new ValidatorException(new FacesMessage("The email message must contain non-whitespace characters."));
        }
    }

}
