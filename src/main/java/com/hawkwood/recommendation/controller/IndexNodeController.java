package com.hawkwood.recommendation.controller;



import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hawkwood.recommendation.util.HSVBinaryTreeImpl;
import com.hawkwood.recommendation.util.ImageProcessor;
import com.hawkwood.recommendation.util.SiftBinaryTreeImpl;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

@Controller
public class IndexNodeController {

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		// Do any additional configuration here
		return builder.build();
	}
	@Autowired
	HSVBinaryTreeImpl HSVBinaryTreeImpl;
	@Autowired
	SiftBinaryTreeImpl SiftBinaryTreeImpl;
	@Autowired
	ImageProcessor ImageProcessor;

	@Autowired
	RestTemplate restTemplate;

	@RequestMapping("/")
	public String index(){

		return "index";
	}
	@RequestMapping("/hsv")
	public String hsv(Model theModel){
		List<String> paths = ImageProcessor.imageNames("/Users/hawkwood/Downloads/style-color-images/style/");
		List<String> show = new ArrayList<>();
		for(int i=0;i<10;i++) {
			 int index = (int) (Math.random()* paths.size());
			 show.add(paths.get(index).replace("/Users/hawkwood/Downloads/style-color-images", "/images"));
		}
		theModel.addAttribute("show", show);
		return "hsv";
	}


	@RequestMapping("/contour")
	public String contour(Model theModel){
		List<String> paths = ImageProcessor.imageNames("/Users/hawkwood/Downloads/style-color-images/style/");
		List<String> show = new ArrayList<>();
		for(int i=0;i<10;i++) {
			 int index = (int) (Math.random()* paths.size());
			 show.add(paths.get(index).replace("/Users/hawkwood/Downloads/style-color-images", "/images"));
		}
		theModel.addAttribute("show", show);
		return "contour";
	}

	@RequestMapping("/querycontour")
	public String querycontour(@RequestParam(name="path") String path, Model theModel){
		String namepath = SiftBinaryTreeImpl.QueryPictures("1.1", 8, path.replace("/images","/Users/hawkwood/Downloads/style-color-images"), null);
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			String[] names = objectMapper.readValue(new File(namepath), String[].class);
			for (int i=0;i<names.length;i++) {

				names[i] = names[i].replace("/Users/hawkwood/Downloads/style-color-images", "/images");
			} 
			theModel.addAttribute("names",names);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "querycontour";
	}
	@RequestMapping("/querydl")
	public String querydl(@RequestParam(name="path") String path, Model theModel) throws InterruptedException{
		String namepath =  path.replace("/images","/Users/hawkwood/Downloads/style-color-images");
		try {

			String[] names = restTemplate.getForObject("http://127.0.0.1:5000/recommendation?path="+namepath, String[].class);
			for(int i=0;i<names.length;i++){
				names[i] = names[i].replace("/Users/hawkwood/Downloads/style-color-images", "/images");
			}
			theModel.addAttribute("names",names);

		} catch (Exception e) {
			e.printStackTrace();
		}


		return "querydl";
	}
//	public String querydl(@RequestParam(name="path") String path, Model theModel) throws InterruptedException{
//		String namepath =  path.replace("/images","/Users/hawkwood/Downloads/style-color-images");
//		try {
//			Process proc;
//			proc = Runtime.getRuntime().exec("python /Users/hawkwood/PycharmProjects/styleclassification/recommendation_Online.py "+namepath);
//			BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
//			String line = null;
//			String[] names = new String[10];
//			int index = 0;
//			while ((line = in.readLine()) != null && index<=10) {
//				if(index>=1)  names[index-1] = line.replace("/Users/hawkwood/Downloads/style-color-images", "/images");
//				index++;
//
//			}
//			in.close();
//
//			proc.waitFor();
//			theModel.addAttribute("names",names);
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//
//
//		return "querydl";
//	}
	@RequestMapping("/dlfeature")
	public String dlfeature(Model theModel){
		List<String> paths = ImageProcessor.imageNames("/Users/hawkwood/Downloads/style-color-images/style/");
		List<String> show = new ArrayList<>();
		for(int i=0;i<10;i++) {
			int index = (int) (Math.random()* paths.size());
			show.add(paths.get(index).replace("/Users/hawkwood/Downloads/style-color-images", "/images"));
		}
		theModel.addAttribute("show", show);
		return "dlfeature";
	}

	@RequestMapping("/query")
	public String query(@RequestParam(name="path") String path, Model theModel){
		String namepath = HSVBinaryTreeImpl.QueryPictures("1.1", 8, path.replace("/images","/Users/hawkwood/Downloads/style-color-images"), null);
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			String[] names = objectMapper.readValue(new File(namepath), String[].class);
			for (int i=0;i<names.length;i++) {
				names[i] = names[i].replace("/Users/hawkwood/Downloads/style-color-images", "/images");
			} 
			theModel.addAttribute("names",names);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "query";
	}
}
