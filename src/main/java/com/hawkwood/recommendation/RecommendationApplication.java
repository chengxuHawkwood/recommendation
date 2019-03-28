package com.hawkwood.recommendation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;

import com.hawkwood.recommendation.util.ImageProcessor;
import com.hawkwood.recommendation.util.ImageProcessorLowlevelImpl;
import com.hawkwood.recommendation.util.TreeBuilder;

import nu.pattern.OpenCV;



@SpringBootApplication
@EnableCaching
public class RecommendationApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(RecommendationApplication.class, args);
		System.out.println(System.getProperty("java.library.path"));
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub			
				OpenCV.loadShared();
				TreeBuilder tb = (TreeBuilder) context.getBean("HSVBinaryTreeImpl");
				tb.BuildTree("/Users/hawkwood/Downloads/style-color-images/style/");
			}
		});
		thread.start();

	}

}
