package com.hawkwood.recommendation.controller;



import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hawkwood.recommendation.util.HSVBinaryTreeImpl;
import com.hawkwood.recommendation.util.ImageProcessor;


@Controller
public class IndexNodeController {
	@Autowired
	HSVBinaryTreeImpl HSVBinaryTreeImpl;
	@Autowired
	ImageProcessor ImageProcessor;
	@RequestMapping("/")
	public String index(Model theModel){
		List<String> paths = ImageProcessor.imageNames("/Users/hawkwood/Downloads/style-color-images/style/");
		List<String> show = new ArrayList<>();
		for(int i=0;i<10;i++) {
			 int index = (int) (Math.random()* paths.size());
			 show.add(paths.get(index).replace("/Users/hawkwood/Downloads/style-color-images", "/images"));
		}
		theModel.addAttribute("show", show);
		return "index";
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

	@RequestMapping("/query")
	public String query(@RequestParam(name="path") String path, Model theModel){
		System.out.println("path"+path);
		String namepath = HSVBinaryTreeImpl.QueryPictures("1.1", 10, path.replace("/images","/Users/hawkwood/Downloads/style-color-images"), null);
		System.out.println(namepath);
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
