package chat.ejb.service;

import javax.ejb.Local;

/**
 * This interface provides functionality associated with user account data.
 *
 * @author Bryan Daniel
 */
@Local
public interface AccountService {

    /**
     * This method generates a username for a new user.
     *
     * @param firstName the user's first name
     * @return the new username
     */
    public String generateUsername(String firstName);

    /**
     * This method uses the entity manager to retrieve the activation status of
     * the user with the given username.
     *
     * @param username the username
     * @return the activation status
     */
    public boolean findActivationStatusByUsername(String username);

    /**
     * This method uses the entity manager to retrieve the activation status of
     * the user with the given registration code.
     *
     * @param registrationCode the registration code
     * @return the activation status
     */
    public boolean findActivationStatusByRegistrationCode(String registrationCode);

    /**
     * This method searches for accounts which already have the given email
     * address.
     *
     * @param emailAddress the email address
     * @return true if an account is found for the given email address, false
     * otherwise
     */
    public boolean emailAddressPreviouslyStored(String emailAddress);

    /**
     * This method locates the appropriate account using the given registration
     * code and activates the account.
     *
     * @param registrationCode the registration code
     * @return the indication of operation success or failure
     */
    public boolean activateAccount(String registrationCode);

    /**
     * This method locates the appropriate account using the given username and
     * activates the account.
     *
     * @param username the username
     * @return the indication of operation success or failure
     */
    public boolean activateAccountByUsername(String username);

    /**
     * This method locates the appropriate account using the given username and
     * deactivates the account.
     *
     * @param username the username
     * @return the indication of operation success or failure
     */
    public boolean deactivateAccountByUsername(String username);
}
