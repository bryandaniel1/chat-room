package chat.web.validator;

import chat.ejb.service.AccountService;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 * This FacesValidator tests email address input to ensure length requirements
 * and uniqueness.
 *
 * @author Bryan Daniel
 */
@FacesValidator("emailAddressValidator")
public class EmailAddressValidator implements Validator {

    /**
     * The account service
     */
    @EJB(beanName = "accountService")
    private AccountService accountService;

    /**
     * This method uses the account service to test whether or not the email
     * address is already used by an existing user. If so, an exception is
     * thrown.
     *
     * @param context the FacesContext object
     * @param component the UIComponent object
     * @param value the email address
     * @throws ValidatorException
     */
    @Override
    public void validate(FacesContext context, UIComponent component, Object value)
            throws ValidatorException {

        String emailAddress = (String) value;

        if (accountService.emailAddressPreviouslyStored(emailAddress)) {
            throw new ValidatorException(new FacesMessage("The given email address is already "
                    + "used by an existing account."));
        }
    }

}
