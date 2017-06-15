package com.nice.sparkathon.walle.service;

import com.ibm.watson.developer_cloud.conversation.v1.ConversationService;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageRequest;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Narendra Kalekar on 6/12/2017.
 */
public class ConversationEngine {

    private ConversationService serviceCS;
    private static final String USERNAME = "02c9d5ab-2d7e-4a39-9fe2-02712c6fbfeb";
    private static final String PASSWORD = "6g6TyoFM0u3u";
    private static final String WORKSPACEID = "23a9fb50-c64b-437c-981e-73c350a37ec3";
    private MessageRequest newMessage;
    private MessageResponse response;

    public ConversationEngine(){
        serviceCS = new ConversationService(ConversationService.VERSION_DATE_2017_02_03);
        serviceCS.setUsernameAndPassword(USERNAME, PASSWORD);
    }

    public String getMessageFromConversationEngine(String inputMessage){
        List<String> outputMessage = new ArrayList<String>();
        newMessage = new MessageRequest.Builder().inputText(inputMessage).build();
        response = serviceCS.message(WORKSPACEID, newMessage).execute();
        outputMessage = response.getText();
      // System.out.println("Conversation Engine "+response.toString());
        return outputMessage.get(0);
    }
}
