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

import chat.ejb.entity.ChatRoomUser;
import chat.ejb.entity.UserSignIn;
import chat.ejb.entity.UserSignInPK;
import chat.ejb.entity.UserSignOut;
import chat.ejb.entity.UserSignOutPK;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ValidationException;
import org.mindrot.jbcrypt.BCrypt;

/**
 * This UserService implementation provides the functionality for operations
 * concerning users of the Enterprise WebSocket Chat application.
 *
 * @author Bryan Daniel
 */
@Stateless(name = "userService")
public class UserServiceImpl implements UserService {
    
    /**
     * The EJB context
     */
    @Resource
    private EJBContext context;

    /**
     * The entity manager for the chat room entities
     */
    @PersistenceContext(unitName = "ChatRoom-ejbPU")
    private EntityManager entityManager;

    /**
     * This method takes the username and password parameters and verifies a
     * match in the database. The password check uses the BCrypt utility to
     * check hashed passwords.
     *
     * @param user the chat room user
     * @return the indication of authentication
     */
    @Override
    public boolean authenticateUser(ChatRoomUser user) {
        if (!userReadyForAuthentication(user)) {
            Logger.getLogger(UserServiceImpl.class.getName()).log(Level.FINER,
                    "UserServiceImpl.authenticateUser: User is not ready for authentication.");
            return false;
        }
        ChatRoomUser storedUser = findUserByUsername(user.getUserCredentials().getUsername());
        if (storedUser == null) {
            Logger.getLogger(UserServiceImpl.class.getName()).log(Level.FINER,
                    "UserServiceImpl.authenticateUser: User could not be found in the database.");
            return false;
        }
        return BCrypt.checkpw(user.getUserCredentials().getPassword(),
                storedUser.getUserCredentials().getPassword());
    }

    /**
     * This method uses the entity manager to retrieve the user associated with
     * the given username.
     *
     * @param username the username
     * @return the user or null if no user could be found
     */
    @Override
    public ChatRoomUser findUserByUsername(String username) {
        Query query = entityManager.createNamedQuery("ChatRoomUser.findByUsername");
        ChatRoomUser user = null;
        try {
            user = (ChatRoomUser) query.setParameter("username", username).getSingleResult();
        } catch (NoResultException nre) {
            Logger.getLogger(UserServiceImpl.class.getName()).log(Level.INFO, 
                    "No user could be found with the username, {0}.", username);
        } catch (Exception e) {
            Logger.getLogger(UserServiceImpl.class.getName()).log(Level.SEVERE,
                    "UserServiceImpl: An exception occurred in the findUserByUsername method.", e);
        }
        return user;
    }
    
    /**
     * This method returns all chat room users from the database.
     * 
     * @return the list of all users
     */
    @Override
    public List<ChatRoomUser> findAllUsers(){
        entityManager.getEntityManagerFactory().getCache().evictAll();
        return entityManager.createNamedQuery("ChatRoomUser.findAllOrderByUsername")
                .getResultList();
    }

    /**
     * If the given user's last sign-in occurred before the user's last
     * sign-out, this method stores the user sign-in event in the database.
     *
     * @param username the username
     */
    @Override
    public void recordSignIn(String username) {
        
        Date lastSignIn = null;
        try {
            lastSignIn = (Date) entityManager.createNamedQuery("UserSignIn.findLastSignIn")
                    .setParameter("username", username).getSingleResult();
            Date lastSignOut = (Date) entityManager.createNamedQuery("UserSignIn.findLastSignOut")
                    .setParameter("username", username).getSingleResult();
            if (lastSignIn.before(lastSignOut)) {                
                entityManager.persist(new UserSignIn(new UserSignInPK(username, new Date())));
            }
        } catch (NoResultException nre) {
            if(lastSignIn == null){
                entityManager.persist(new UserSignIn(new UserSignInPK(username, new Date())));
            }
        } catch (Exception e) {
            Logger.getLogger(UserServiceImpl.class.getName()).log(Level.SEVERE,
                    "UserServiceImpl: An exception occurred in the recordSignIn method.", e);
        }

    }

    /**
     * If the given user's last sign-in occurred after the user's last
     * sign-out, this method stores the user sign-out event in the database.
     *
     * @param username the username
     */
    @Override
    public void recordSignOut(String username) {
        
        Date lastSignOut = null;
        try {
            Date lastSignIn = (Date) entityManager.createNamedQuery("UserSignIn.findLastSignIn")
                    .setParameter("username", username).getSingleResult();
            lastSignOut = (Date) entityManager.createNamedQuery("UserSignIn.findLastSignOut")
                    .setParameter("username", username).getSingleResult();
            if (lastSignIn.after(lastSignOut)) {
                entityManager.persist(new UserSignOut(new UserSignOutPK(username, new Date())));
            }
        } catch (NoResultException nre) {
            if (lastSignOut == null) {
                entityManager.persist(new UserSignOut(new UserSignOutPK(username, new Date())));
            }
        } catch (Exception e) {
            Logger.getLogger(UserServiceImpl.class.getName()).log(Level.SEVERE,
                    "UserServiceImpl: An exception occurred in the recordSignOut method.", e);
        }

    }

    /**
     * This method uses the entity manager to save a chat room user.
     *
     * @param user the chat room user
     * @return the indication of operation success or failure
     */
    @Override
    public boolean persist(ChatRoomUser user) {
        try {
            entityManager.persist(user);
            entityManager.flush();
            return true;
        } catch (ValidationException ve) {
            Logger.getLogger(UserServiceImpl.class.getName()).log(Level.SEVERE,
                    "UserServiceImpl: A ValidationException occurred in the persist method: {0}", 
                    ve.getMessage());
        } catch (Exception e) {
            Logger.getLogger(UserServiceImpl.class.getName()).log(Level.SEVERE,
                    "UserServiceImpl: An Exception occurred in the persist method: {0}", 
                    e.getMessage());
            context.setRollbackOnly();
        }
        return false;
    }

    /**
     * This method ensures that the given user is prepared for the
     * authentication check.
     *
     * @param user the chat room user
     * @return the indication of readiness
     */
    private boolean userReadyForAuthentication(ChatRoomUser user) {
        return ((user != null) && (user.getUserCredentials() != null)
                && (user.getUserCredentials().getPassword() != null)
                && (user.getUserCredentials().getUsername() != null));
    }
}
