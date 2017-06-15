package com.nice.sparkathon.walle.service;

import com.ibm.watson.developer_cloud.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneCategory;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneScore;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by Narendra Kalekar on 6/12/2017.
 */
public class ResponseAnalyser {

  //  private ToneAnalyzer serviceTA;
    private static final String USERNAME = "46fe9ba8-a976-4dd6-aa07-654a58369976";
    private static final String PASSWORD = "cq3PiCAMDzrk";

 /*   public ResponseAnalyser(){
        serviceTA  = new ToneAnalyzer(ToneAnalyzer.VERSION_DATE_2016_05_19);
        serviceTA.setUsernameAndPassword(USERNAME, PASSWORD);
    }*/

    public String getMessageFromResponseAnalyser(String inputMessage){
        return getEmotionalToneOfInputText(inputMessage);
       // System.out.println("Responce Analyser "+tone.toString());
    }

    private String getEmotionalToneOfInputText(String inputText){

              /*
              ToneOptions toneOption = new ToneOptions.Builder()
                           .html(false)
                           .addTone(Tone.EMOTION)
                           .addTone(Tone.LANGUAGE)
                           .addTone(Tone.SOCIAL)
                           .build();

              ToneAnalyzer toneAnalyzer = new ToneAnalyzer(ToneAnalyzer.VERSION_DATE_2016_05_19);
              toneAnalyzer.setUsernameAndPassword(WatsonConfiguration.getToneAnalyzerUserName(), WatsonConfiguration.getToneAnalyzerPassword());
              ToneAnalysis tone = toneAnalyzer.getTone(inputText, toneOption).execute();
              System.out.println(">>>>>TONE"+tone);
              */
        List<ToneScore> toneScores = new ArrayList<>();
        ToneAnalyzer toneAnalyzer = new ToneAnalyzer(ToneAnalyzer.VERSION_DATE_2016_05_19);
        toneAnalyzer.setUsernameAndPassword(USERNAME, PASSWORD);
        ToneAnalysis tone = toneAnalyzer.getTone(inputText, null).execute();
        List<ToneCategory> toneCategoryList = tone.getDocumentTone().getTones();

        for(ToneCategory toneCategory : toneCategoryList){
            //ToneCategory toneCategory = toneCategoryList.get(0);

            if(toneCategory.getName().equalsIgnoreCase("Emotion Tone")){
                toneScores = toneCategory.getTones();

            }
        }
        //tone.getDocumentTone().getTones().subList(0, )
        //Get Emotional Tone only as of now
      //  System.out.println("Response Analyser "+toneScores);

        return getProminentEmotion(toneScores);

    }




    private String getProminentEmotion(List<ToneScore> toneScores){

        TreeMap<Double, String> toneMap = new TreeMap<Double, String>();

        if(toneScores==null){
            return "none";
        }else{

            for(ToneScore tonescore : toneScores){
                toneMap.put(tonescore.getScore(), tonescore.getName());
            }

        }
       // System.out.print();
        if(toneMap.lastKey().equals(0.0))
            return "none";
        return toneMap.get(toneMap.lastKey());
    }


/*
    serviceTA.setUsernameAndPassword("46fe9ba8-a976-4dd6-aa07-654a58369976", "cq3PiCAMDzrk");
    String text = "my laptop is not working";
    ToneAnalysis tone = serviceTA.getTone(text, null).execute();
        System.out.println(tone);*/
}
