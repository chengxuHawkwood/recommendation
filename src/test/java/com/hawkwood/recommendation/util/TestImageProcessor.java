package com.hawkwood.recommendation.util;

import java.util.List;

import org.hibernate.dialect.identity.Ingres10IdentityColumnSupport;

import nu.pattern.OpenCV;
import smile.clustering.KMeans;
import weka.core.mathematicalexpression.sym;

public class TestImageProcessor {
public static void main(String[] args) {
	OpenCV.loadShared();
	//System.load("/usr/local/Cellar/opencv/4.0.1/share/java/opencv4/libopencv_java401.dylib");
	ImageProcessor imageProcessor = new ImageProcessorLowlevelImpl();
	System.out.println(imageProcessor.getfeaturesComparsion("/Users/hawkwood/Downloads/style-color-images/style/0_0_001.png", "/Users/hawkwood/Downloads/style-color-images/style/0_0_002.png", 0));
	double[] res = imageProcessor.getHSVfeatures("/Users/hawkwood/Downloads/style-color-images/style/0_0_001.png");
//	for(int i=0;i<res.length;i++) System.out.println(res[i]);
	 List<String> names = imageProcessor.imageNames("/Users/hawkwood/Downloads/style-color-images/style/");
	 double[] testsizeInput = imageProcessor.getHSVfeatures(names.get(0));
	 double[][] input = new double[names.size()][testsizeInput.length];
	 for(int i=0;i<names.size();i++) 
		 input[i] = imageProcessor.getHSVfeatures(names.get(i));
//	 KMeans kMeans= new KMeans(input, 2);
//	 System.out.println(kMeans.centroids()[0].length);
//	 System.out.println(kMeans.getClusterLabel().length);
//	 System.out.println(kMeans.getClusterSize()[0]);
//	 for(int i=0;i<kMeans.getClusterLabel().length;i++)
//		 System.out.println(kMeans.getClusterLabel()[i]);
//	 
	}
}
