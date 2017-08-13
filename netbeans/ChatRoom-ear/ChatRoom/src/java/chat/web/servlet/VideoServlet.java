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
package chat.web.servlet;

import chat.ejb.service.FileHandlingService;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.omnifaces.servlet.FileServlet;

/**
 * This HttpServlet class extends FileServlet to return a video stored in the
 * file system, identified by the request parameters.
 *
 * @author Bryan Daniel
 */
@WebServlet(name = "VideoServlet", urlPatterns = {"/video"})
public class VideoServlet extends FileServlet {

    /**
     * Serial version UID
     */
    private static final long serialVersionUID = 1288801299278497573L;

    /**
     * The video handling service
     */
    @EJB(beanName = "videoHandler")
    private FileHandlingService videoHandler;

    /**
     * This method calls the doGet method of the superclass. The doGet method of
     * the superclass is overridden to keep stack traces concerning frequent
     * closed connections from being printed to the logs.
     *
     * @param request the HttpServletRequest object
     * @param response the HttpServletResponse object
     * @throws ServletException if the request for the GET could not be handled
     * @throws IOException if an input or output error is detected when the
     * servlet handles the GET request
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            super.doGet(request, response);
        } catch (IOException | IllegalArgumentException e) {
            String message = (e instanceof IOException) ? "An IOException was thrown in VideoServlet. {0}"
                    : "An IllegalArgumentException was thrown in VideoServlet. {0}";
            Logger.getLogger(VideoServlet.class.getName()).log(Level.INFO, message, e.getMessage());
        }
    }

    /**
     * This method finds and returns the file identified by the given request
     * parameters.
     *
     * @param request the request object
     * @return the file
     * @throws IllegalArgumentException if any request parameters are invalid
     */
    @Override
    protected File getFile(HttpServletRequest request) throws IllegalArgumentException {

        File videoFile = null;
        String username = request.getParameter("user");
        String videoNumberString = request.getParameter("video");
        Long videoNumber = null;

        if (username == null || videoNumberString == null) {
            Logger.getLogger(ImageServlet.class.getName()).log(Level.SEVERE,
                    "The parameters, user and video, were not part of the request.");
            throw new IllegalArgumentException();
        }
        try {
            videoNumber = Long.parseLong(videoNumberString);
        } catch (NumberFormatException nfe) {
            Logger.getLogger(VideoServlet.class.getName()).log(Level.SEVERE,
                    "The video parameter is not valid; it should be a number.", nfe);
            throw new IllegalArgumentException();
        }

        try {
            videoFile = (File) videoHandler.retrieveFromFileSystem(username, videoNumber);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(VideoServlet.class.getName()).log(Level.SEVERE,
                    "A FileNotFoundException was thrown in the VideoServlet.", ex);
        }

        return videoFile;
    }
}
