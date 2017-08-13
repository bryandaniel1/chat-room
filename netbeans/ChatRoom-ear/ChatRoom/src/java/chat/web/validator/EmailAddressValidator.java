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
     * @throws ValidatorException if validation fails
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
