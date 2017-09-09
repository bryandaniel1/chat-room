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
package chat.web.model;

import chat.ejb.entity.ChatRoomUser;
import chat.ejb.util.EncryptionUtil;
import chat.ejb.util.PropertiesUtil;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

/**
 * This EmailMessenger implementation is used for sending messages written by
 * the administrator to specific users.
 *
 * @author Bryan Daniel
 */
public class AdministratorEmailMessenger implements EmailMessenger {

    /**
     * The primary message builder
     */
    private final StringBuilder primaryMessageBuilder = new StringBuilder();

    /**
     * The alternate message builder
     */
    private final StringBuilder alternateMessageBuilder = new StringBuilder();

    /**
     * The new chat room user
     */
    private ChatRoomUser recipient;

    /**
     * The message
     */
    private String message;

    /**
     * The constructor sets the values for the new user and the activation link
     * and also builds the administrator messages.
     *
     * @param recipient the new user
     * @param message the message
     */
    public AdministratorEmailMessenger(ChatRoomUser recipient, String message) {

        this.recipient = recipient;
        this.message = message;

        primaryMessageBuilder.append("<h3>Administrator Message</h3>");
        primaryMessageBuilder.append("<p>{0}</p>");

        alternateMessageBuilder.append("Administrator Message\n");
        alternateMessageBuilder.append("{0}\n");
    }

    /**
     * This method sends an email message from the administrator to the
     * specified recipient.
     *
     * @param recipientAddress the new user's email address
     * @return the indication of operation success or failure
     */
    @Override
    public boolean sendMail(String recipientAddress) {

        String password = EncryptionUtil.decrypt(PropertiesUtil.getEncryptedEmailPassword());

        if (password == null) {
            Logger.getLogger(RegistrationEmailMessenger.class.getName()).log(Level.SEVERE,
                    "Mail could not be sent.  A problem with decryption occurred.");
            return false;
        }

        try {
            HtmlEmail email = new HtmlEmail();
            email.setHostName(PropertiesUtil.getEmailHost());
            email.setSmtpPort(465);
            email.setSSLOnConnect(true);
            email.addTo(recipientAddress, recipient.getFirstName() + " " + recipient.getLastName());
            email.setAuthenticator(new DefaultAuthenticator(PropertiesUtil.getEmailAddress(),
                    password));
            email.setFrom(PropertiesUtil.getEmailAddress(), "The ChatRoom Team");
            email.setSubject("ChatRoom Administrator Message");

            // set the primary HTML message
            email.setHtmlMsg(MessageFormat.format(primaryMessageBuilder.toString(), message));

            // set the alternative text message
            email.setTextMsg(MessageFormat.format(alternateMessageBuilder.toString(), message));

            email.send();

        } catch (EmailException ex) {
            Logger.getLogger(AdministratorEmailMessenger.class.getName()).log(Level.SEVERE,
                    "An exception occurred while trying to send the administrator message in the sendMail method.", ex);
            return false;
        }
        return true;
    }

    /**
     * This method does a basic check of an input string to validate that it is
     * an email address.
     *
     * @param emailAddressInput the input string
     * @return the result indicating a valid or invalid string
     */
    public static boolean isEmailAddressValid(String emailAddressInput) {

        try {
            InternetAddress emailAddress = new InternetAddress(emailAddressInput);
            emailAddress.validate();
        } catch (AddressException ex) {
            Logger.getLogger(AdministratorEmailMessenger.class.getName()).log(Level.INFO, null, ex);
            return false;
        }
        return true;
    }

    /**
     * If an error occurs in the sendNewUserEmail method, this method is called
     * to log the error and send a message back to the user interface.
     *
     * @param to the recipient
     * @param from the source
     * @param subject the subject
     */
    public static void logEmailError(String to, String from, String subject) {

        StringBuilder sb = new StringBuilder();
        sb.append(": Unable to send email. \n");
        sb.append("TO: ");
        sb.append(to);
        sb.append("\nFROM: ");
        sb.append(from);
        sb.append("\nSUBJECT: ");
        sb.append(subject);
        sb.append("\n\n");
        Logger.getLogger(AdministratorEmailMessenger.class.getName()).log(Level.SEVERE, sb.toString());
    }

    /**
     * Get the value of recipient
     *
     * @return the value of recipient
     */
    public ChatRoomUser getRecipient() {
        return recipient;
    }

    /**
     * Set the value of recipient
     *
     * @param recipient new value of recipient
     */
    public void setRecipient(ChatRoomUser recipient) {
        this.recipient = recipient;
    }

    /**
     * Get the value of message
     *
     * @return the value of message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Set the value of message
     *
     * @param message new value of message
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
