package chat.web.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 * This FacesValidator class tests 2 password input fields for equality.
 *
 * @author Bryan Daniel
 */
@FacesValidator(value = "passwordMatchValidator")
public class PasswordMatchValidator implements Validator {

    /**
     * This validation method ensures that the both password entries are not
     * null before comparing the 2nd password entry with the 1st password entry.
     * The method throws a ValidatorException if the two values are unequal.
     *
     * @param context
     * @param component
     * @param value
     * @throws ValidatorException
     */
    @Override
    public void validate(FacesContext context, UIComponent component, Object value) 
            throws ValidatorException {

        Object firstEntry = component.getAttributes().get("firstEntry");

        if (value != null && firstEntry != null) {
            if (!value.equals(firstEntry)) {
                throw new ValidatorException(new FacesMessage("The two password entries must match."));
            }
        }
    }

}
