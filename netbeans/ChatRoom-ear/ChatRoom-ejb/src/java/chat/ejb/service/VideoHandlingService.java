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

import chat.ejb.model.VideoMessenger;
import chat.ejb.util.PropertiesUtil;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.servlet.http.Part;

/**
 * This file handling service implementation contains functionality for handling
 * read and write operations for video files.
 *
 * @author Bryan Daniel
 */
@Stateless(name = "videoHandler")
public class VideoHandlingService implements FileHandlingService {

    /**
     * The length of the buffer used for file upload
     */
    public static final int BUFFER_LENGTH = 8192;

    /**
     * The managed executor service
     */
    @Resource
    private ManagedExecutorService managedExecutorService;

    /**
     * This method saves the given video file to the file system. First, it
     * utilizes PropertiesUtil to locate the videos directory. Then, inside the
     * videos directory, it creates a numbered directory under a directory named
     * after the user to hold the new video. If the file is saved successfully,
     * the number of the video directory is returned.
     *
     * @param username the username
     * @param videoName the name of the video file
     * @param messenger the video messenger
     * @return the number designated for the user's video
     * @throws IllegalArgumentException if invalid parameters are provided
     */
    @Override
    public Long saveToFileSystem(String username, String videoName, Object messenger)
            throws IllegalArgumentException {

        if (!(messenger instanceof VideoMessenger)) {
            throw new IllegalArgumentException("A VideoMessenger object is required for the saveToFileSystem operation.");
        }

        long videoNumber = 0L;
        String videosLocation = PropertiesUtil.getLocation(PropertiesUtil.VIDEOS_LOCATION);

        // creating the top-level videos folder if it does not exist
        File videosDirectory = new File(videosLocation);
        if (!videosDirectory.exists() || !videosDirectory.isDirectory()) {
            videosDirectory.mkdir();
        }

        // creating a user folder under videos
        videosDirectory = new File(videosLocation + File.separator + username);
        if (!videosDirectory.exists() || !videosDirectory.isDirectory()) {
            videosDirectory.mkdir();
        }

        // determining the number for the new folder
        if (videosDirectory.list().length == 0) {
            videoNumber++;
        } else {
            for (File videoFolder : videosDirectory.listFiles()) {
                if (videoFolder.isDirectory()) {
                    try {
                        Long existingNumber = Long.parseLong(videoFolder.getName());
                        videoNumber = (existingNumber.compareTo(videoNumber) > 0)
                                ? existingNumber : videoNumber;
                    } catch (NumberFormatException nfe) {
                        Logger.getLogger(VideoHandlingService.class.getName()).log(Level.SEVERE,
                                "Folder name in user images is not a number.");
                    }
                }
            }
            videoNumber++;
        }
        Long finalVideoNumber = videoNumber;

        // creating the numbered folder to hold the video
        videosDirectory = new File(videosDirectory, finalVideoNumber.toString());
        if (!videosDirectory.exists() || !videosDirectory.isDirectory()) {
            videosDirectory.mkdir();
        }

        // creating the video file and writing to the file system
        File newFile = new File(videosDirectory, videoName);
        VideoMessenger videoMessenger = (VideoMessenger) messenger;

        // This potentially long-running process is launched asynchronously.
        CompletableFuture.supplyAsync(() -> uploadVideo(videoMessenger.getVideoToUpload(),
                newFile, finalVideoNumber), managedExecutorService)
                .thenAccept((Long newVideoNumber) -> videoMessenger.relayResults(newVideoNumber));

        Logger.getLogger(VideoHandlingService.class.getName()).log(Level.INFO,
                "Beginning the video upload for {0}.", username);

        return finalVideoNumber;
    }

    /**
     * This method uses the given username and item number to locate and return
     * a saved video file.
     *
     * @param username the username
     * @param videoNumber the identifying number of the video
     * @return the file to retrieve
     * @throws java.io.FileNotFoundException if the video is not found
     */
    @Override
    public Object retrieveFromFileSystem(String username, Long videoNumber)
            throws FileNotFoundException {

        String videosLocation = PropertiesUtil.getLocation(PropertiesUtil.VIDEOS_LOCATION);
        File videoDirectory = new File(videosLocation + File.separator + username
                + File.separator + videoNumber);
        if (!videoDirectory.exists()) {
            throw new FileNotFoundException("The path to the video could not be found.");
        }
        File video = null;
        for (File file : videoDirectory.listFiles()) {
            Logger.getLogger(VideoHandlingService.class.getName()).log(Level.INFO,
                    "Name of file retrieved: {0}", file.getName());
            video = file;
            break;
        }
        if (video == null) {
            throw new FileNotFoundException("The video file could not be found.");
        }
        return video;
    }

    /**
     * The method opens input and output streams to read the video data from the
     * Part object and write it to the file system.
     *
     * @param video the video to save
     * @param newFile the new file
     * @param videoNumber the identifying video number
     * @return the identifying video number or null if the process is
     * unsuccessful
     */
    private Long uploadVideo(Part video, File newFile, Long videoNumber) {

        try (InputStream inputStream = ((Part) video).getInputStream();
                OutputStream outputStream = new FileOutputStream(newFile)) {

            byte[] buffer = new byte[BUFFER_LENGTH];
            while (inputStream.read(buffer) != -1) {
                outputStream.write(buffer);
            }
            Logger.getLogger(VideoHandlingService.class.getName()).log(Level.INFO,
                    "Video upload is complete and the video file is located in {0}.",
                    newFile.getPath());
            return videoNumber;
        } catch (IOException ex) {
            Logger.getLogger(VideoHandlingService.class.getName()).log(Level.SEVERE,
                    "An exception occurred while uploading the video.", ex);
        }
        return null;
    }
}
