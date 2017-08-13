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
package chat.ejb.service;

import chat.ejb.entity.UserAccount;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;

/**
 * This AccountService implementation provides functionality to handle user
 * account data.
 *
 * @author Bryan Daniel
 */
@Stateless(name = "accountService")
public class AccountServiceImpl implements AccountService {

    /**
     * The entity manager for the chat room entities
     */
    @PersistenceContext(unitName = "ChatRoom-ejbPU")
    private EntityManager entityManager;

    /**
     * This method calls a stored procedure to generate a username for a new
     * user.
     *
     * @param firstName the user's first name
     * @return the new username
     */
    @Override
    public String generateUsername(String firstName) {

        StoredProcedureQuery query = entityManager.createNamedStoredProcedureQuery("generateUsername");
        query.setParameter("first_name_in", firstName);
        query.execute();

        return (String) query.getOutputParameterValue("username");
    }

    /**
     * This method uses the entity manager to retrieve the activation status of
     * the user with the given username.
     *
     * @param username the username
     * @return the activation status
     */
    @Override
    public boolean findActivationStatusByUsername(String username) {

        Query query = entityManager.createNamedQuery("UserAccount.findByUsername");
        try {
            UserAccount userAccount = (UserAccount) query.setParameter("username", username)
                    .getSingleResult();
            return userAccount.getActivated();
        } catch (NoResultException nre) {
            Logger.getLogger(AccountServiceImpl.class.getName()).log(Level.SEVERE,
                    "AccountServiceImpl: A user was not found with the given username - {0}.", username);
        } catch (Exception e) {
            Logger.getLogger(AccountServiceImpl.class.getName()).log(Level.SEVERE,
                    "AccountServiceImpl: An exception occurred in the findActivationStatusByUsername "
                    + "method.", e);
        }

        return false;
    }

    /**
     * This method uses the entity manager to retrieve the activation status of
     * the user with the given registration code.
     *
     * @param registrationCode the registration code
     * @return the activation status
     */
    @Override
    public boolean findActivationStatusByRegistrationCode(String registrationCode) {

        Query query = entityManager.createNamedQuery("UserAccount.findByRegistrationCode");
        try {
            UserAccount userAccount = (UserAccount) query.setParameter("registrationCode", registrationCode)
                    .getSingleResult();
            return userAccount.getActivated();
        } catch (NoResultException nre) {
            Logger.getLogger(AccountServiceImpl.class.getName()).log(Level.SEVERE,
                    "AccountServiceImpl: A user was not found with the given registration code - {0}.",
                    registrationCode);
        } catch (Exception e) {
            Logger.getLogger(AccountServiceImpl.class.getName()).log(Level.SEVERE,
                    "AccountServiceImpl: An exception occurred in the findActivationStatusByRegistrationCode "
                    + "method.", e);
        }

        return false;
    }

    /**
     * This method searches for accounts which already have the given email
     * address.
     *
     * @param emailAddress the email address
     * @return true if an account is found for the given email address, false
     * otherwise
     */
    @Override
    public boolean emailAddressPreviouslyStored(String emailAddress) {

        Query query = entityManager.createNamedQuery("UserAccount.findByEmailAddress");
        try {
            query.setParameter("emailAddress", emailAddress).getSingleResult();
            return true;
        } catch (NoResultException nre) {
            Logger.getLogger(AccountServiceImpl.class.getName()).log(Level.FINER,
                    "Email address, {0}, not previously stored.", emailAddress);
        } catch (Exception e) {
            Logger.getLogger(AccountServiceImpl.class.getName()).log(Level.SEVERE,
                    "AccountServiceImpl: An exception occurred in the emailAddressPreviouslyStored method.", e);
        }

        return false;
    }

    /**
     * This method locates the appropriate account using the given registration
     * code and activates the account.
     *
     * @param registrationCode the registration code
     * @return the indication of operation success or failure
     */
    @Override
    public boolean activateAccount(String registrationCode) {

        Query query = entityManager.createNamedQuery("UserAccount.activateAccount");
        try {
            int numberUpdated = query.setParameter("registrationCode", registrationCode)
                    .executeUpdate();
            if (numberUpdated == 1) {
                return true;
            }
        } catch (Exception e) {
            Logger.getLogger(AccountServiceImpl.class.getName()).log(Level.SEVERE,
                    "AccountServiceImpl: An exception occurred in the activateAccount method.", e);
        }

        return false;
    }

    /**
     * This method locates the appropriate account using the given username and
     * activates the account.
     *
     * @param username the username
     * @return the indication of operation success or failure
     */
    @Override
    public boolean activateAccountByUsername(String username) {

        Query query = entityManager.createNamedQuery("UserAccount.activateAccountByUsername");
        try {
            int numberUpdated = query.setParameter("username", username)
                    .executeUpdate();
            if (numberUpdated == 1) {
                return true;
            }
        } catch (Exception e) {
            Logger.getLogger(AccountServiceImpl.class.getName()).log(Level.SEVERE,
                    "AccountServiceImpl: An exception occurred in the activateAccountByUsername method.", e);
        }

        return false;
    }

    /**
     * This method locates the appropriate account using the given username and
     * deactivates the account.
     *
     * @param username the username
     * @return the indication of operation success or failure
     */
    @Override
    public boolean deactivateAccountByUsername(String username) {

        Query query = entityManager.createNamedQuery("UserAccount.deactivateAccountByUsername");
        try {
            int numberUpdated = query.setParameter("username", username)
                    .executeUpdate();
            if (numberUpdated == 1) {
                return true;
            }
        } catch (Exception e) {
            Logger.getLogger(AccountServiceImpl.class.getName()).log(Level.SEVERE,
                    "AccountServiceImpl: An exception occurred in the deactivateAccountByUsername method.", e);
        }

        return false;
    }
}
