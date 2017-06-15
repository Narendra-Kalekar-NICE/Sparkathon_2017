package com.nice.sparkathon.walle.service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Narendra Kalekar on 6/12/2017.
 */
public class ResponseCreator {

    private static final Map<String,String> toneMapper;
    static {
        toneMapper = new HashMap<String,String>();
        toneMapper.put("Sadness","Sorry to hear that, don't worry we will help you.");
        toneMapper.put("Anger","We are really sorry, we will try our best to solve your problem");
        toneMapper.put("Disgust","We are extremely sorry for inconvinience you faced.");
        toneMapper.put("Joy","Great!!!");
        toneMapper.put("Fear","Don't worry.We are here to help you.");
    }

    public String getMessageFromResponseCreator(String emotion, String response){
        String emotionMsg="";
        String finalMSG="";
      //  System.out.println("EMO "+emotion);
        if(emotion.equals("none")) {
            finalMSG=response;
           // System.out.println("Response Creator IF "+finalMSG);
        }else
        {
            emotionMsg = toneMapper.get(emotion);
            finalMSG = emotionMsg + " " +response;
           // System.out.println("Response Creator ELSE "+finalMSG);
        }
      //  System.out.println("Response Creator "+finalMSG);
        return finalMSG;
    }
}
