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
     * @throws ValidatorException if validation fails
     */
    @Override
    public void validate(FacesContext context, UIComponent component, Object value)
            throws ValidatorException {

        if (value != null && ((String) value).trim().length() == 0) {
            throw new ValidatorException(new FacesMessage("The email message must contain non-whitespace characters."));
        }
    }

}
