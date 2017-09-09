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
 * This EmailMessenger implementation is used for sending messages to a newly
 * registered user, prompting the user to visit the link in the message to
 * activate the account.
 *
 * @author Bryan Daniel
 */
public class RegistrationEmailMessenger implements EmailMessenger {

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
     * The account activation link
     */
    private String activationLink;

    /**
     * The constructor sets the values for the new user and the activation link
     * and also builds the registration messages.
     *
     * @param recipient the new user
     * @param registrationCode the registration code
     */
    public RegistrationEmailMessenger(ChatRoomUser recipient, String registrationCode) {

        this.recipient = recipient;
        this.activationLink = MessageFormat.format("http://localhost:8080/ChatRoom/account?action=activate&code={0}", 
                registrationCode);

        primaryMessageBuilder.append("<h3>New User Account</h3>");
        primaryMessageBuilder.append("<p>Hello {0},</p>");
        primaryMessageBuilder.append("<p>Thank you for registering with ChatRoom.  Your username is {1}.  Please visit the link below to activate this registration.</p>");
        primaryMessageBuilder.append("<a href=\"{2}\" target=\"_blank\">Activate Now</a>");

        alternateMessageBuilder.append("New User Account\n");
        alternateMessageBuilder.append("Hello {0},\n");
        alternateMessageBuilder.append("Thank you for registering with ChatRoom.  Your username is {1}.  Please visit the URL below to activate this registration.\n");
        alternateMessageBuilder.append("{2}");
    }

    /**
     * This method sends an email message to a prospective chat room user to
     * allow the user to activate the new registration.
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
            email.setSubject("ChatRoom Registration");

            // set the primary HTML message
            email.setHtmlMsg(MessageFormat.format(primaryMessageBuilder.toString(),
                    recipient.getFirstName(), recipient.getUsername(), activationLink));

            // set the alternative text message
            email.setTextMsg(MessageFormat.format(alternateMessageBuilder.toString(),
                    recipient.getFirstName(), recipient.getUsername(), activationLink));

            email.send();

        } catch (EmailException ex) {
            Logger.getLogger(RegistrationEmailMessenger.class.getName()).log(Level.SEVERE,
                    "An exception occurred while trying to send the registration message in the sendMail method.", ex);
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
            Logger.getLogger(RegistrationEmailMessenger.class.getName()).log(Level.INFO, null, ex);
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
        Logger.getLogger(RegistrationEmailMessenger.class.getName()).log(Level.SEVERE, sb.toString());
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
     * Get the value of activationLink
     *
     * @return the value of activationLink
     */
    public String getActivationLink() {
        return activationLink;
    }

    /**
     * Set the value of activationLink
     *
     * @param activationLink new value of activationLink
     */
    public void setActivationLink(String activationLink) {
        this.activationLink = activationLink;
    }
}
