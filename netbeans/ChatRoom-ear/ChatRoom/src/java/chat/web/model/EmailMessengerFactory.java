package chat.web.model;

import chat.ejb.entity.ChatRoomUser;

/**
 * This class builds and delivers email messengers.
 *
 * @author Bryan Daniel
 */
public class EmailMessengerFactory {

    /**
     * This method passes the ChatRoomUser and the registration code to the
     * constructor of the RegistrationEmailMessenger object and returns this new
     * object.
     *
     * @param recipient the user
     * @param registrationCode the registration code
     * @return the email messenger
     */
    public EmailMessenger produceRegistrationEmailMessenger(ChatRoomUser recipient,
            String registrationCode) {
        return new RegistrationEmailMessenger(recipient, registrationCode);
    }
    
    /**
     * This method passes the ChatRoomUser and the administrator message to the
     * constructor of the AdministratorEmailMessenger object and returns this new
     * object.
     *
     * @param recipient the user
     * @param message the message
     * @return the email messenger
     */
    public EmailMessenger produceAdministratorEmailMessenger(ChatRoomUser recipient,
            String message) {
        return new AdministratorEmailMessenger(recipient, message);
    }
    
}
