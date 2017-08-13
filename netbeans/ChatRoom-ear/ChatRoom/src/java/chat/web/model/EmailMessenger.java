package chat.web.model;

/**
 * This interface provides functionality to send an email message.
 *
 * @author Bryan Daniel
 */
public interface EmailMessenger {

    /**
     * This method sends an email from a host email address to a recipient's
     * address.
     *
     * @param recipientAddress the new user's email address
     * @return the indication of operation success or failure
     */
    public boolean sendMail(String recipientAddress);
}
