package com.nice.sparkathon.walle.controller;

import com.ibm.watson.developer_cloud.service.exception.ServiceResponseException;
import com.nice.sparkathon.walle.model.InputMessage;
import com.nice.sparkathon.walle.model.OutputMessage;
import com.nice.sparkathon.walle.service.ConversationEngine;
import com.nice.sparkathon.walle.service.InputPreProcessor;
import com.nice.sparkathon.walle.service.ResponseAnalyser;
import com.nice.sparkathon.walle.service.ResponseCreator;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ConversationManager {

    private ConversationEngine conversationEngine = new ConversationEngine();
    private InputPreProcessor inputPreProcessor = new InputPreProcessor();
    private ResponseAnalyser responseAnalyser = new ResponseAnalyser();
    private ResponseCreator responseCreator = new ResponseCreator();
    private static int counter = 0;

    @MessageMapping("/conversation")
    @SendTo("/wall-e/chat")
    public OutputMessage talkToWallE(InputMessage inputMessage) throws Exception {

        System.out.println("================================================================================");
        System.out.println("User Input : \n\t"+inputMessage.getMessage());
        System.out.println("================================================================================");

        String outputPreprocessor="";
        try {
            inputPreProcessor.analyzeUserInput(inputMessage.getMessage());
            outputPreprocessor = inputPreProcessor.latestResultToString();
        }catch (ServiceResponseException sre){
            outputPreprocessor = inputMessage.getMessage();
        }

        System.out.println("Output from Input Pre Processor : \n\t "+ outputPreprocessor);
        System.out.println("================================================================================");

        String outputCovEngine = conversationEngine.getMessageFromConversationEngine(outputPreprocessor.trim());

        String outputResponceAnalyser = responseAnalyser.getMessageFromResponseAnalyser(inputMessage.getMessage());

        System.out.println("Output from Tone Analyser : \n\t\""+outputResponceAnalyser+"\"");
        System.out.println("================================================================================");

        System.out.println("Output from Conversation Engine : \n\t"+outputCovEngine);
        System.out.println("================================================================================");

        String outputMessage = responseCreator.getMessageFromResponseCreator(outputResponceAnalyser,outputCovEngine);

        System.out.println("Output from Response Creator : \n\t"+outputMessage);
        System.out.println("================================================================================");

        int pr = 0;
        switch (counter){
            case 0:
                pr = 10;
                break;
            case 1:
                pr = 7;
                break;
            case 2:
                pr = 5;
                break;
            case 3:
                pr = 9;
                break;
            case 4:
                pr = 999;
                break;
        }
        counter++;
        System.out.println("Queue Scheduler Sequence Number : "+pr);
        System.out.println("================================================================================");

        return new OutputMessage(outputMessage);
    }
}
