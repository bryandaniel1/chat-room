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

import chat.ejb.entity.Message;
import chat.ejb.model.Conversation;
import chat.ejb.service.ChatRoomService;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 * This managed bean supports the operations and UI components of the
 * conversation history page.
 *
 * @author Bryan Daniel
 */
@Named(value = "historyBean")
@ViewScoped
public class HistoryBean implements Serializable {

    /**
     * Serial version UID
     */
    private static final long serialVersionUID = -3156263170671495324L;

    /**
     * The user bean
     */
    @Inject
    private UserBean userBean;

    /**
     * The chat room service
     */
    @EJB(beanName = "chatRoomService")
    private ChatRoomService chatRoomService;

    /**
     * The user's previous conversations
     */
    private List<Conversation> conversations;

    /**
     * The selected conversation index
     */
    private BigDecimal selectedConversationIndex;

    /**
     * The selected conversation
     */
    private Conversation selectedConversation;

    /**
     * The messages of the selected conversation
     */
    private List<Message> conversationMessages;

    /**
     * This method is executed after construction to collect the user's previous
     * conversations.
     */
    @PostConstruct
    public void init() {
        conversations = chatRoomService.findUserConversations(userBean.getUser().getUsername());
    }

    /**
     * This method uses the chat room service to find messages for a
     * conversation.
     */
    public void findAllConversationMessages() {

        for (Conversation c : conversations) {
            if (c.getIndex().compareTo(selectedConversationIndex) == 0) {
                setSelectedConversation(c);
                break;
            }
        }

        if (getSelectedConversation() != null) {
            conversationMessages = chatRoomService
                    .getAllConversationMessages(getSelectedConversation().getDateOccurred(),
                            getSelectedConversation().getRoomName(), userBean.getUser());
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("The selected conversation could not be retrieved."));
        }
    }

    /**
     * This method returns a URI to load an image in the chat log.
     *
     * @param uriPattern the partial URI containing the request parameters
     * @return the image URI
     */
    public String getImageURI(String uriPattern) {

        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();
        String requestURL = request.getRequestURL().toString();
        String requestURI = request.getRequestURI();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(requestURL.substring(0, requestURL.length() - requestURI.length()));
        stringBuilder.append(request.getContextPath());
        stringBuilder.append("/");
        stringBuilder.append(uriPattern);

        return stringBuilder.toString();
    }

    /**
     * Get the value of userBean
     *
     * @return the value of userBean
     */
    public UserBean getUserBean() {
        return userBean;
    }

    /**
     * Set the value of userBean
     *
     * @param userBean new value of userBean
     */
    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }

    /**
     * Get the value of conversations
     *
     * @return the value of conversations
     */
    public List<Conversation> getConversations() {
        return conversations;
    }

    /**
     * Set the value of conversations
     *
     * @param conversations new value of conversations
     */
    public void setConversations(List<Conversation> conversations) {
        this.conversations = conversations;
    }

    /**
     * Get the value of selectedConversationIndex
     *
     * @return the value of selectedConversationIndex
     */
    public BigDecimal getSelectedConversationIndex() {
        return selectedConversationIndex;
    }

    /**
     * Set the value of selectedConversationIndex
     *
     * @param selectedConversationIndex new value of selectedConversationIndex
     */
    public void setSelectedConversationIndex(BigDecimal selectedConversationIndex) {
        this.selectedConversationIndex = selectedConversationIndex;
    }

    /**
     * Get the value of selectedConversation
     *
     * @return the value of selectedConversation
     */
    public Conversation getSelectedConversation() {
        return selectedConversation;
    }

    /**
     * Set the value of selectedConversation
     *
     * @param selectedConversation new value of selectedConversation
     */
    public void setSelectedConversation(Conversation selectedConversation) {
        this.selectedConversation = selectedConversation;
    }

    /**
     * Get the value of conversationMessages
     *
     * @return the value of conversationMessages
     */
    public List<Message> getConversationMessages() {
        return conversationMessages;
    }

    /**
     * Set the value of conversationMessages
     *
     * @param conversationMessages new value of conversationMessages
     */
    public void setConversationMessages(List<Message> conversationMessages) {
        this.conversationMessages = conversationMessages;
    }
}
