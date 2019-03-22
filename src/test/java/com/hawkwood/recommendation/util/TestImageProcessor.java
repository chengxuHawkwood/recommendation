package com.hawkwood.recommendation.util;

import org.opencv.core.Core;


public class TestImageProcessor {
public static void main(String[] args) {
	System.load("/usr/local/Cellar/opencv/4.0.1/share/java/opencv4/libopencv_java401.dylib");
	ImageProcessor imageProcessor = new ImageProcessorLowlevelImpl();
	System.out.println(imageProcessor.getfeaturesComparsion("/Users/hawkwood/Downloads/style-color-images/style/0_0_001.png", "/Users/hawkwood/Downloads/style-color-images/style/0_0_002.png", 1));
}
}
