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
     * @param context the faces context
     * @param component the UI component
     * @param value the value to validate
     * @throws ValidatorException if validation fails
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
