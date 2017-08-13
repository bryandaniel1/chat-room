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
    private static final StringBuilder PRIMARY_MESSAGE_BUILDER = new StringBuilder();

    /**
     * The alternate message builder
     */
    private static final StringBuilder ALTERNATE_MESSAGE_BUILDER = new StringBuilder();

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

        PRIMARY_MESSAGE_BUILDER.append("<h3>New User Account</h3>");
        PRIMARY_MESSAGE_BUILDER.append("<p>Hello {0},</p>");
        PRIMARY_MESSAGE_BUILDER.append("<p>Thank you for registering with ChatRoom.  Your username is {1}.  Please visit the link below to activate this registration.</p>");
        PRIMARY_MESSAGE_BUILDER.append("<a href=\"{2}\" target=\"_blank\">Activate Now</a>");

        ALTERNATE_MESSAGE_BUILDER.append("New User Account\n");
        ALTERNATE_MESSAGE_BUILDER.append("Hello {0},\n");
        ALTERNATE_MESSAGE_BUILDER.append("Thank you for registering with ChatRoom.  Your username is {1}.  Please visit the URL below to activate this registration.\n");
        ALTERNATE_MESSAGE_BUILDER.append("{2}");
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
            email.setHtmlMsg(MessageFormat.format(PRIMARY_MESSAGE_BUILDER.toString(),
                    recipient.getFirstName(), recipient.getUsername(), activationLink));

            // set the alternative text message
            email.setTextMsg(MessageFormat.format(ALTERNATE_MESSAGE_BUILDER.toString(),
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
