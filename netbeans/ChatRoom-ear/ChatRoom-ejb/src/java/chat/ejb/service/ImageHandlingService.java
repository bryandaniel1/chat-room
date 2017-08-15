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

import chat.ejb.util.PropertiesUtil;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.imageio.ImageIO;

/**
 * This file handling service implementation contains functionality for handling
 * read and write operations for image files.
 *
 * @author Bryan Daniel
 */
@Stateless(name = "imageHandler")
public class ImageHandlingService implements FileHandlingService {

    /**
     * This method saves the given picture file to the file system. First, it
     * utilizes PropertiesUtil to locate the images directory. Then, inside the
     * images directory, it creates a numbered directory under a directory named
     * after the user to hold the new picture. If the image is saved
     * successfully, the number of the image directory is returned.
     *
     * @param username the username
     * @param imageName the image name
     * @param picture the picture
     * @return the number designated for the user's image or null if an
     * exception occurs
     * @throws IllegalArgumentException if invalid parameters are provided
     */
    @Override
    public Long saveToFileSystem(String username, String imageName, Object picture)
            throws IllegalArgumentException {

        if (!(picture instanceof BufferedImage)) {
            throw new IllegalArgumentException("A BufferedImage object is required for the saveToFileSystem operation.");
        }

        Long imageNumber = 0L;
        String imagesLocation = PropertiesUtil.getLocation(PropertiesUtil.IMAGES_LOCATION);

        // creating the top-level images folder if it does not exist
        File imagesDirectory = new File(imagesLocation);
        if (!imagesDirectory.exists() || !imagesDirectory.isDirectory()) {
            imagesDirectory.mkdir();
        }

        // creating a user folder under images
        imagesDirectory = new File(imagesLocation + File.separator + username);
        if (!imagesDirectory.exists() || !imagesDirectory.isDirectory()) {
            imagesDirectory.mkdir();
        }

        // determining the number for the new folder
        if (imagesDirectory.list().length == 0) {
            imageNumber++;
        } else {
            for (File imageFolder : imagesDirectory.listFiles()) {
                if (imageFolder.isDirectory()) {
                    try {
                        Long existingNumber = Long.parseLong(imageFolder.getName());
                        imageNumber = (existingNumber.compareTo(imageNumber) > 0)
                                ? existingNumber : imageNumber;
                    } catch (NumberFormatException nfe) {
                        Logger.getLogger(ImageHandlingService.class.getName()).log(Level.SEVERE,
                                null, "Folder name in user images is not a number.");
                    }
                }
            }
            imageNumber++;
        }

        // creating the numbered folder to hold the picture
        imagesDirectory = new File(imagesDirectory, imageNumber.toString());
        if (!imagesDirectory.exists() || !imagesDirectory.isDirectory()) {
            imagesDirectory.mkdir();
        }

        // creating the picture file and saving the picture
        File newFile = new File(imagesDirectory, imageName);
        try {
            ImageIO.write((BufferedImage) picture, "png", newFile);
            return imageNumber;
        } catch (IOException ex) {
            Logger.getLogger(ImageHandlingService.class.getName()).log(Level.SEVERE,
                    "An exception occured while writing an image in the savePicture method.", ex);
        }
        return null;
    }

    /**
     * This method uses the given username and image number to locate and return
     * a saved image as a BufferedImage object.
     *
     * @param username the username
     * @param imageNumber the image number
     * @return the image or null if an exception occurs
     * @throws java.io.FileNotFoundException if the image is not found
     */
    @Override
    public BufferedImage retrieveFromFileSystem(String username, Long imageNumber)
            throws FileNotFoundException {

        String imagesLocation = PropertiesUtil.getLocation(PropertiesUtil.IMAGES_LOCATION);
        File imageDirectory = new File(imagesLocation + File.separator + username
                + File.separator + imageNumber);
        if (!imageDirectory.exists()) {
            throw new FileNotFoundException("The path to the image could not be found.");
        }
        File image = null;
        for (File file : imageDirectory.listFiles()) {
            Logger.getLogger(ImageHandlingService.class.getName()).log(Level.INFO,
                    "Name of file retrieved: {0}", file.getName());
            image = file;
            break;
        }
        if (image == null) {
            throw new FileNotFoundException("The image file could not be found.");
        }
        try {
            return ImageIO.read(image);
        } catch (IOException ex) {
            Logger.getLogger(ImageHandlingService.class.getName()).log(Level.SEVERE,
                    "An exception occured while reading an image in the retrievePictureFromFile method.", ex);
        }
        return null;
    }
}
