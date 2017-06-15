/**
 * Copyright 2017 IBM Corp. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package com.nice.sparkathon.walle.service;


import com.ibm.watson.developer_cloud.natural_language_understanding.v1.NaturalLanguageUnderstanding;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.*;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.Tone;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneOptions;

import java.util.*;

public class InputPreProcessor {


	private AnalysisResults latestResult;
	private TreeMap<String,Map<Double, String>> keywordEmotionMap = new TreeMap<String, Map<Double,String>>();
	private List<String> keywordList = new ArrayList<String>();
	private List<String> categoriesList = new ArrayList<String>();
	private static final String USERNAME = "b29b2395-edc8-444d-ae37-4483f684f558";
	private static final String PASSWORD = "0NlKPifIejHR";
	private static final float CATEGORY_RELEVANCE_THRESHOLD = (float) 0.5;
	private static final float KEYWORD_RELEVANCE_THRESHOLD=(float) 0.5;

	public AnalysisResults analyzeUserInput(String inputText) {


		NaturalLanguageUnderstanding service = new NaturalLanguageUnderstanding(
				NaturalLanguageUnderstanding.VERSION_DATE_2017_02_27,
				USERNAME,
				PASSWORD
				);


		//ConceptsOptions
		ConceptsOptions conceptOptions = new ConceptsOptions.Builder()
				.limit(10)
				.build();

		//CategoriesOptions
		CategoriesOptions categoriesOptions = new CategoriesOptions();

		//SemanticOptions
		SemanticRolesOptions semanticRoleOptions = new SemanticRolesOptions.Builder()
				.entities(true)
				.keywords(true)
				.limit(10)
				.build();

		EntitiesOptions entitiesOptions = new EntitiesOptions.Builder()
				.emotion(true)
				.sentiment(true)
				.limit(10)
				.build();

		KeywordsOptions keywordsOptions = new KeywordsOptions.Builder()
				.emotion(true)
				.sentiment(true)
				.limit(10)
				.build();

		Features features = new Features.Builder()
				.entities(entitiesOptions)
				.keywords(keywordsOptions)
				.concepts(conceptOptions)
				.categories(categoriesOptions)
				.semanticRoles(semanticRoleOptions)
				.build();

		AnalyzeOptions parameters = new AnalyzeOptions.Builder()
				.text(inputText)
				.features(features)
				.build();

		AnalysisResults response = service
				.analyze(parameters)
				.execute();
//		System.out.println(response);
//		System.out.println("Analyzed Text==>"+response.getAnalyzedText());
//		System.out.println("Categories==>"+response.getCategories());
//		System.out.println("Concepts==>"+response.getConcepts());
//		System.out.println("Emotion==>"+response.getEmotion());
//		System.out.println("Entities==>"+response.getEntities());
//		System.out.println("Keywords==>"+response.getKeywords());
//		System.out.println("Semantic==>"+response.getSemanticRoles());

		this.latestResult = response;
		return response;

	}
	
	//TODO : tone analysis is supposed to be done in 
	public String getToneOfInputText(String inputText){
		
		 ToneOptions toneOption = new ToneOptions.Builder()
				 .html(false)
				 .addTone(Tone.EMOTION)
				 .addTone(Tone.LANGUAGE)
				 .addTone(Tone.SOCIAL)
				 .build();
				
		 ToneAnalyzer toneAnalyzer = new ToneAnalyzer(ToneAnalyzer.VERSION_DATE_2016_05_19);
		 toneAnalyzer.setUsernameAndPassword(USERNAME, PASSWORD);
 		 ToneAnalysis tone = toneAnalyzer.getTone(inputText, toneOption).execute();
 		 System.out.println(">>>>>TONE"+tone);
		System.out.println(">>>>>TONE"+tone);
 		 
 		 return "";
 				 
	}

	public String latestResultToString(){


		StringBuilder responseData = new StringBuilder();

		//Detected Language
		String language="";
		language = getLanguage(latestResult);
		if(language!=null){
			responseData.append("\""+language+"\"");
		}else{
			responseData.append("#lang_none");
		}
		responseData.append(" ");

		//Get Keywords above threshold
		keywordList.clear();
		keywordList = getKeywordsAboveThreshold(latestResult);
		for(String keyword : keywordList){
			responseData.append("\""+keyword+"\"");
			responseData.append(" ");

		}

		//Get Categories above threshold
		StringBuilder tempResponseData = new StringBuilder(responseData);
		categoriesList.clear();
		categoriesList = getCategories(latestResult);
		for(String subCategory : categoriesList){
			responseData.append("\""+subCategory+"\"");
			responseData.append(" ");
			tempResponseData.append("\""+subCategory+"\"");
			tempResponseData.append(" ");

		}
		//System.out.println("Input Pre Processor :"+tempResponseData.toString());

		String responseString = responseData.toString();
		responseData.delete(0, responseData.length());

		return responseString;
	}

	private List<String> getCategories(AnalysisResults latestResult){

		//Get Categories above threshold
		List<String> categoryList = new ArrayList<String>();
		List<CategoriesResult> tmpCategoryResponseList = latestResult.getCategories();
		if(tmpCategoryResponseList==null || tmpCategoryResponseList.isEmpty()){
			//responseData.append("#categories_none");
		}else{
			for(CategoriesResult categoryResult : tmpCategoryResponseList){
				if(categoryResult.getScore()>CATEGORY_RELEVANCE_THRESHOLD){

					List<String> categorySubList = Arrays.asList(categoryResult.getLabel().split("/"));
					for(String subCategory : categorySubList){
						categoryList.add(subCategory);
						//responseData.append("\""+subCategory+"\"");
						//responseData.append(" ");
					}
				}
			}
		}

		return categoryList;
	}

	private TreeMap<Double, String> getProminentEmotion(EmotionScores emotionScores){

		TreeMap<Double, String> keywordMap = new TreeMap<Double, String>();
		keywordMap.clear();
		TreeMap<Double, String> emotionMap = new TreeMap<Double, String>();

		if(emotionScores==null){
			keywordMap.put((double) 0,"none");
		}else{

			emotionMap.put(emotionScores.getAnger(), "Anger");
			emotionMap.put(emotionScores.getDisgust(), "Disgust");
			emotionMap.put(emotionScores.getFear(), "Fear");
			emotionMap.put(emotionScores.getJoy(), "Joy");
			emotionMap.put(emotionScores.getSadness(), "Sad");
			keywordMap.put(emotionMap.lastKey(), emotionMap.get(emotionMap.lastKey()));
		}

		return keywordMap;
	}

	private List<String> getKeywordsAboveThreshold(AnalysisResults latestResult){

		List<String> keywordListString = new ArrayList<String>();
		List<KeywordsResult> keywordList = latestResult.getKeywords();
		if(keywordList==null || keywordList.isEmpty()){
			//responseData.append("#keywords_none");
		}else{
			for(KeywordsResult keyword : keywordList){
				if(keyword.getRelevance()>KEYWORD_RELEVANCE_THRESHOLD){

					keywordListString.add(keyword.getText());
					keywordEmotionMap.put(keyword.getText(), getProminentEmotion(keyword.getEmotion()));
				}
			}
		}

		return keywordListString;

	}

	private String getLanguage(AnalysisResults latestResult){
		return latestResult.getLanguage();

	}
	
	

}
