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
     * @param context the faces context
     * @param component the UI component
     * @param value the value to validate
     * @throws ValidatorException if validation fails
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
