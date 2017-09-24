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
package chat.web.bean;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 * This class holds the text value for the Chat Room description to be displayed
 * on the sign-in page.
 *
 * @author Bryan Daniel
 */
@Named(value = "aboutBean")
@ViewScoped
public class AboutBean implements Serializable {

    /**
     * Serial version UID
     */
    private static final long serialVersionUID = 1509724299128644827L;

    /**
     * The description to inform new users of the chat room
     */
    private static final String DESCRIPTION = "The chat room is a great place to talk with friends, "
            + "share pictures and videos, and read previous chat room conversations.";

    /**
     * The variable to hold the description
     */
    private String aboutChatRoom;

    /**
     * Executed after construction
     */
    @PostConstruct
    public void init() {
        aboutChatRoom = null;
    }

    /**
     * This method sets the text for the description of the chat room.
     */
    public void toggleDescription() {
        if (aboutChatRoom == null) {
            setAboutChatRoom(DESCRIPTION);
        } else {
            setAboutChatRoom(null);
        }
    }

    /**
     * Get the value of aboutChatRoom
     *
     * @return the value of aboutChatRoom
     */
    public String getAboutChatRoom() {
        return aboutChatRoom;
    }

    /**
     * Set the value of aboutChatRoom
     *
     * @param aboutChatRoom new value of aboutChatRoom
     */
    public void setAboutChatRoom(String aboutChatRoom) {
        this.aboutChatRoom = aboutChatRoom;
    }
}
