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
package chat.ejb.util;

import chat.ejb.service.ImageHandlingService;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * This class contains utility methods for accessing property values for the
 * application.
 *
 * @author Bryan Daniel
 */
public class PropertiesUtil {

    /**
     * The key for the images location
     */
    public static final String IMAGES_LOCATION = "imagesLocation";

    /**
     * The key for the videos location
     */
    public static final String VIDEOS_LOCATION = "videosLocation";

    /**
     * The key for the chat records location
     */
    public static final String CHAT_RECORDS_LOCATION = "chatRecordsLocation";

    /**
     * The key for the email host
     */
    public static final String EMAIL_HOST = "emailHost";

    /**
     * The key for the email address
     */
    public static final String EMAIL_ADDRESS = "emailAddress";

    /**
     * The key for the encrypted email password
     */
    public static final String ENCRYPTED_EMAIL_PASSWORD = "encryptedEmailPassword";

    /**
     * The map holding application properties
     */
    private static final HashMap<String, String> APPLICATION_PROPERTIES = new HashMap<>();

    /**
     * The location of the chat room configuration file is a property set in
     * GlassFish System Properties with this key.
     */
    private static final String CONFIGURATION_PROPERTIES_FILE_KEY = "chatroom-config";

    /**
     * The index for a single element
     */
    private static final int SINGLE_ELEMENT = 0;

    /**
     * This method retrieves the path of the directory for the given location
     * type.
     *
     * @param type the location type
     * @return the path of the images directory
     */
    public static String getLocation(String type) {

        String location = null;

        switch (type) {
            case IMAGES_LOCATION:
                location = APPLICATION_PROPERTIES.get(IMAGES_LOCATION);
                break;
            case VIDEOS_LOCATION:
                location = APPLICATION_PROPERTIES.get(VIDEOS_LOCATION);
                break;
            case CHAT_RECORDS_LOCATION:
                location = APPLICATION_PROPERTIES.get(CHAT_RECORDS_LOCATION);
                break;
            default:
                break;
        }

        return location;
    }

    /**
     * This method returns the email host value.
     *
     * @return the value of the email host
     */
    public static String getEmailHost() {
        return APPLICATION_PROPERTIES.get(EMAIL_HOST);
    }

    /**
     * This method returns the email address value.
     *
     * @return the value of the email address
     */
    public static String getEmailAddress() {
        return APPLICATION_PROPERTIES.get(EMAIL_ADDRESS);
    }

    /**
     * This method returns the encrypted email password value.
     *
     * @return the value of the encrypted email password
     */
    public static String getEncryptedEmailPassword() {
        return APPLICATION_PROPERTIES.get(ENCRYPTED_EMAIL_PASSWORD);
    }

    /**
     * This method reads the configuration file to find the property values to
     * be loaded into the map of application properties.
     *
     * @return the configuration property value
     * @throws javax.xml.parsers.ParserConfigurationException if a
     * DocumentBuilder cannot be created which satisfies the configuration
     * requested
     * @throws org.xml.sax.SAXException if any parse errors occur
     * @throws java.io.IOException if any IO errors occur
     */
    public static String loadProperties() throws ParserConfigurationException,
            SAXException, IOException {

        final String imagesElement = "path-to-images";
        final String videosElement = "path-to-videos";
        final String chatRecordsElement = "path-to-chat-records";
        final String propertyValue = "value";
        final String emailElement = "email";
        final String hostValue = "host";
        final String addressValue = "address";
        final String passwordValue = "password";
        String[] elements = new String[]{imagesElement, videosElement, chatRecordsElement,
            emailElement};

        String configurationFilePath = System.getProperty(CONFIGURATION_PROPERTIES_FILE_KEY);
        InputStream input = new FileInputStream(new File(configurationFilePath));
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(input);

        /* Normalize the nodes */
        doc.getDocumentElement().normalize();

        for (String element : elements) {

            /* Find the element */
            NodeList nodeList = doc.getElementsByTagName(element);

            /* There should only be one node in the list of elements. */
            Node n = nodeList.item(SINGLE_ELEMENT);

            if (n.getNodeType() == Node.ELEMENT_NODE) {

                Element e = (Element) n;
                switch (element) {
                    case imagesElement:
                        APPLICATION_PROPERTIES.put(IMAGES_LOCATION, e.getElementsByTagName(propertyValue)
                                .item(SINGLE_ELEMENT).getTextContent());
                        break;
                    case videosElement:
                        APPLICATION_PROPERTIES.put(VIDEOS_LOCATION, e.getElementsByTagName(propertyValue)
                                .item(SINGLE_ELEMENT).getTextContent());
                        break;
                    case chatRecordsElement:
                        APPLICATION_PROPERTIES.put(CHAT_RECORDS_LOCATION, e.getElementsByTagName(propertyValue)
                                .item(SINGLE_ELEMENT).getTextContent());
                        break;
                    case emailElement:
                        APPLICATION_PROPERTIES.put(EMAIL_HOST, e.getElementsByTagName(hostValue)
                                .item(SINGLE_ELEMENT).getTextContent());
                        APPLICATION_PROPERTIES.put(EMAIL_ADDRESS, e.getElementsByTagName(addressValue)
                                .item(SINGLE_ELEMENT).getTextContent());
                        APPLICATION_PROPERTIES.put(ENCRYPTED_EMAIL_PASSWORD, e.getElementsByTagName(passwordValue)
                                .item(SINGLE_ELEMENT).getTextContent());
                        break;
                    default:
                        break;
                }
            }
        }

        try {
            input.close();
        } catch (IOException ex) {
            Logger.getLogger(ImageHandlingService.class.getName()).log(Level.SEVERE,
                    "An exception occurred in loadProperties method.", ex);
        }

        return propertyValue;
    }
}
