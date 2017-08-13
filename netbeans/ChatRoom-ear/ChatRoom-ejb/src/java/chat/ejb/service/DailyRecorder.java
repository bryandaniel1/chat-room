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
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

/**
 * This singleton session bean contains the functionality for scheduled daily
 * recording of chat messages.
 *
 * @author Bryan Daniel
 */
@Singleton
@Startup
public class DailyRecorder {

    /**
     * The delimiter for CSV records
     */
    private static final String NEW_LINE = "\n";

    /**
     * The CSV header
     */
    private static final Object[] HEADER = {"room name", "room creator", "username",
        "first name", "last name", "user role", "message", "time written"};

    /**
     * The entity manager
     */
    @PersistenceContext(unitName = "ChatRoom-ejbPU")
    private EntityManager em;

    /**
     * This scheduled method records the chat messages of the past day in a CSV
     * file.
     *
     * The messages of the past day are retrieved from the database, then
     * written to a CSV file identified by the date of the previous day. The
     * directory path of the saved files is set in the configuration file, the
     * location of which is stored as a system property in the GlassFish server.
     */
    @Schedule(hour = "0")
    public void writeDailyChatRecord() {

        SimpleDateFormat logFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        Logger.getLogger(DailyRecorder.class.getName()).log(Level.INFO,
                "{0} The writeDailyChatRecord process is starting.", logFormatter.format(new Date()));

        FileWriter fileWriter = null;
        CSVPrinter csvPrinter = null;

        Query q = em.createNativeQuery("SELECT cr.room_name, cr.room_creator, u.username, "
                + "u.first_name, u.last_name, u.user_role, m.message, m.time_written "
                + "FROM ChatRoom cr, ChatRoomUser u, Message m "
                + "WHERE cr.room_name = m.room_name "
                + "AND m.username = u.username "
                + "AND m.time_written >= DATE_SUB(NOW(),INTERVAL 1 day) "
                + "ORDER BY cr.room_name, m.time_written;");

        try {
            String recordsFolder = PropertiesUtil.getLocation(PropertiesUtil.CHAT_RECORDS_LOCATION);

            // creating the top-level records folder if it does not exist
            File recordsDirectory = new File(recordsFolder);
            if (!recordsDirectory.exists() || !recordsDirectory.isDirectory()) {
                recordsDirectory.mkdir();
            }

            // creating the new file identified by yesterday's date
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -1);
            String newFileName = "chat-records-" + formatter.format(calendar.getTime()) + ".csv";
            File recordsFile = new File(recordsFolder + File.separator + newFileName);

            // writing the CSV file header
            CSVFormat csvFormat = CSVFormat.DEFAULT.withRecordSeparator(NEW_LINE);
            fileWriter = new FileWriter(recordsFile);
            csvPrinter = new CSVPrinter(fileWriter, csvFormat);
            csvPrinter.printRecord(HEADER);

            // retrieving and writing records to the file
            List<Object[]> results = q.getResultList();
            for (Object[] record : results) {
                csvPrinter.printRecord(record);
            }
            Logger.getLogger(DailyRecorder.class.getName()).log(Level.INFO,
                    "{0} The writeDailyChatRecord completed successfully, writing the file, {1}.",
                    new Object[]{logFormatter.format(new Date()), newFileName});
        } catch (IOException ex) {
            Logger.getLogger(DailyRecorder.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.flush();
                    fileWriter.close();
                } catch (IOException ex) {
                    Logger.getLogger(DailyRecorder.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (csvPrinter != null) {
                try {
                    csvPrinter.close();
                } catch (IOException ex) {
                    Logger.getLogger(DailyRecorder.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
