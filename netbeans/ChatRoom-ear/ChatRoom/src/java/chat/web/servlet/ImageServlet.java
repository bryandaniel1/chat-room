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
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

/**
 * This HttpServlet class contains the functionality for retrieving image data
 * from an upload or a file and writing that data to the response.
 *
 * @author Bryan Daniel
 */
@WebServlet(name = "ImageServlet", urlPatterns = {"/image"})
public class ImageServlet extends HttpServlet {

    /**
     * The key for the image value
     */
    public static final String IMAGE_KEY = "image";

    /**
     * Serial version UID
     */
    private static final long serialVersionUID = 5672625652451519035L;

    /**
     * The image handling service
     */
    @EJB(beanName = "imageHandler")
    private FileHandlingService imageHandler;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if (action != null) {
            switch (action) {
                case "preview":
                    HttpSession session = request.getSession();
                    sendPhoto(response, getPhotoBytesFromPart((Part) session.getAttribute(IMAGE_KEY)));
                    break;
                case "view":
                    String username = request.getParameter("user");
                    sendPhoto(response, getPhotoBytesFromFile(username,
                            Long.parseLong(request.getParameter("image"))));
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * This method converts the given Part object to a byte array used to
     * display the image.
     *
     * @param part the Part object
     * @return the byte array
     */
    private byte[] getPhotoBytesFromPart(Part part) {

        byte[] photoBytes = null;
        ByteArrayOutputStream outputStream = null;
        try {
            try (InputStream is = part.getInputStream()) {
                outputStream = new ByteArrayOutputStream();
                int i;
                while ((i = is.read()) != -1) {
                    outputStream.write(i);
                }
                photoBytes = outputStream.toByteArray();
            }
        } catch (Exception ex) {
            photoBytes = null;
            Logger.getLogger(ImageServlet.class.getName()).log(Level.SEVERE,
                    "An exception occurred in the getPhotoBytesFromPart method.", ex);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException ex) {
                    Logger.getLogger(ImageServlet.class.getName()).log(Level.SEVERE,
                            "An exception occurred in the getPhotoBytesFromPart method while closing resources.", ex);
                }
            }
        }
        return photoBytes;
    }

    /**
     * This method uses the given username and image number to retrieve the
     * image file and return the byte array for the image.
     *
     * @param username the username
     * @param imageNumber the number identifying the image
     * @return the byte array
     */
    private byte[] getPhotoBytesFromFile(String username, Long imageNumber) {

        byte[] imageBytes = null;
        BufferedImage image = null;
        ByteArrayOutputStream outputStream = null;
        try {
            image = (BufferedImage) imageHandler.retrieveFromFileSystem(username, imageNumber);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ImageServlet.class.getName()).log(Level.SEVERE,
                    "An exception occurred in the getPhotoBytesFromFile method while retrieving the picture from the file system.", ex);
        }
        if (image != null) {
            try {
                outputStream = new ByteArrayOutputStream();
                ImageIO.write(image, "png", outputStream);
                imageBytes = outputStream.toByteArray();
            } catch (IOException ex) {
                Logger.getLogger(ImageServlet.class.getName()).log(Level.SEVERE,
                        "An exception occurred in the getPhotoBytesFromFile method while writing the image to output.", ex);
            } finally {
                if (outputStream != null) {
                    try {
                        outputStream.flush();
                        outputStream.close();
                    } catch (IOException ex) {
                        Logger.getLogger(ImageServlet.class.getName()).log(Level.SEVERE,
                                "An exception occurred in the getPhotoBytesFromFile method while closing resources.", ex);
                    }
                }
            }
        }
        return imageBytes;
    }

    /**
     * This method performs the task of sending the photo data to the user.
     *
     * @param response the HttpServletResponse object
     * @param photo the photo data
     * @throws IOException if an IO exception occurs
     */
    private void sendPhoto(HttpServletResponse response, byte[] photo) throws IOException {

        response.setContentType("image/png");
        try (OutputStream out = response.getOutputStream()) {
            for (int i = 0; i < photo.length; i++) {
                out.write(photo[i]);
            }
        } catch (IOException ex) {
            Logger.getLogger(ImageServlet.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
    }
}
