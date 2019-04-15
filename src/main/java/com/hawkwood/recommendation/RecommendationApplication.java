package com.hawkwood.recommendation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;

import com.hawkwood.recommendation.util.ImageProcessor;
import com.hawkwood.recommendation.util.ImageProcessorLowlevelImpl;
import com.hawkwood.recommendation.util.TreeBuilder;




@SpringBootApplication
@EnableCaching
public class RecommendationApplication {
	public static void main(String[] args) {
		System.load("/Users/hawkwood/Downloads/opencv-4.0.1/build/lib/libopencv_java401.dylib");
		ConfigurableApplicationContext context = SpringApplication.run(RecommendationApplication.class, args);
//		Thread thread = new Thread(new Runnable() {
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				TreeBuilder tb = (TreeBuilder) context.getBean("siftBinaryTreeImpl");
//				tb.BuildTree("/Users/hawkwood/Downloads/style-color-images/style/");
//				tb = (TreeBuilder) context.getBean("HSVBinaryTreeImpl");
//				tb.BuildTree("/Users/hawkwood/Downloads/style-color-images/style/");
//
//			}
//		});
//		thread.start();

	}

}
