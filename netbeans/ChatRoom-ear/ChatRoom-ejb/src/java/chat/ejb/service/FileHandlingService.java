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
