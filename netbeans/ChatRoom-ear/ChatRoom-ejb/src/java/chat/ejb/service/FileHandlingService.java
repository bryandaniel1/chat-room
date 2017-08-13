package chat.ejb.service;

import java.io.FileNotFoundException;
import javax.ejb.Local;

/**
 * This interface contains methods for handling file read and write operations.
 *
 * @author Bryan Daniel
 */
@Local
public interface FileHandlingService {

    /**
     * This method saves the given object to the file system.
     *
     * @param username the username
     * @param itemName the item name
     * @param itemToSave the item to save
     * @return the number designated for the user's image
     */
    public Long saveToFileSystem(String username, String itemName, Object itemToSave)
            throws IllegalArgumentException;

    /**
     * This method uses the given username and item number to locate and return
     * a saved file.
     *
     * @param username the username
     * @param itemNumber the identifying number of the item
     * @return the file to retrieve
     * @throws java.io.FileNotFoundException if the file is not found
     */
    public Object retrieveFromFileSystem(String username, Long itemNumber)
            throws FileNotFoundException;
}
