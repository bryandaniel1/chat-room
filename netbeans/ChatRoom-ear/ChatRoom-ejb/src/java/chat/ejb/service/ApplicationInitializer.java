package chat.ejb.service;

import chat.ejb.util.PropertiesUtil;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 * This singleton performs essential functions on start up of the application.
 *
 * @author Bryan Daniel
 */
@Singleton
@Startup
public class ApplicationInitializer {

    /**
     * This method executes after construction to load property values from
     * configuration files into memory.
     */
    @PostConstruct
    public void initialize() {
        try {
            PropertiesUtil.loadProperties();
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(ApplicationInitializer.class.getName()).log(Level.SEVERE,
                    "An exception was caught in the initialize method.", ex);
        }
    }
}
