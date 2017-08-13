package chat.web.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 * This FacesValidator class tests password input to ensure it conforms to the
 * required pattern.
 *
 * @author Bryan Daniel
 */
@FacesValidator
public class PasswordPatternValidator implements Validator {

    /**
     * The password pattern contains at least one lower-case letter, one
     * upper-case letter, one number, one special character, no spaces, and is
     * between 8 and 20 characters inclusive.
     */
    private static final String PASSWORD_PATTERN
            = "((?=.*\\d)(?=.*[a-z])(?=\\S+$)(?=.*[A-Z])"
            + "(?=.*[!\"#$%&'()*+,-./:;<=>?@\\[\\]^_`{|}~]).{8,20})";

    /**
     * This method checks the input value against the regular expression. If it
     * does not match the required pattern, a ValidatorException is thrown.
     *
     * @param context
     * @param component
     * @param value
     * @throws ValidatorException
     */
    @Override
    public void validate(FacesContext context, UIComponent component, Object value)
            throws ValidatorException {

        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher((String) value);
        if (!matcher.matches()) {
            throw new ValidatorException(new FacesMessage("The password must contain at "
                    + "least one lower-case letter, one upper-case letter, one number, "
                    + "one special character, no spaces, and have a length between 8 and "
                    + "20 characters inclusive."));
        }
    }

}
