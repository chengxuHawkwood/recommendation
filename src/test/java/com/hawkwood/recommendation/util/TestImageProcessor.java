package com.hawkwood.recommendation.util;

import java.util.List;

import org.hibernate.dialect.identity.Ingres10IdentityColumnSupport;


import com.hawkwood.recommendation.util.ImageProcessor;
import com.hawkwood.recommendation.util.ImageProcessorLowlevelImpl;

//import nu.pattern.OpenCV;
import smile.clustering.KMeans;
import weka.core.mathematicalexpression.sym;

public class TestImageProcessor {
public static void main(String[] args) {
	
//	System.load("/Users/hawkwood/Downloads/opencv-4.0.1/build/lib/libopencv_java401.dylib");
//	System.load("/usr/local/Cellar/opencv/4.0.1/lib/libopencv_xfeatures2d.4.0.1.dylib");
	ImageProcessor imageProcessor = new ImageProcessorLowlevelImpl();
	System.out.println(imageProcessor.getfeaturesComparsion("/Users/hawkwood/Downloads/style-color-images/style/0_0_001.png", "/Users/hawkwood/Downloads/style-color-images/style/0_0_002.png", 0));
	double[] res = imageProcessor.getHSVfeatures("/Users/hawkwood/Downloads/style-color-images/style/0_0_001.png");
 // 	for(int i=0;i<res.length;i++) System.out.print(res[i]);
	 List<String> names = imageProcessor.imageNames("/Users/hawkwood/Downloads/style-color-images/style/");
	 double[] testsizeInput = imageProcessor.getHSVfeatures(names.get(0));
	 double[][] input = new double[names.size()][testsizeInput.length];
	 for(int i=0;i<names.size();i++) 
		 input[i] = imageProcessor.getHSVfeatures(names.get(i));
	 double[] res2 = imageProcessor.getContourfeatures("/Users/hawkwood/Downloads/style-color-images/style/0_0_001.png");
  	 for(int i=0;i<res2.length;i++) System.out.print(res2[i]);
	 double[][] input2 = new double[names.size()][res2.length];
	 for(int i=0;i<names.size();i++) {
		 input2[i] = imageProcessor.getContourfeatures(names.get(i));
	 }	
	 
		 
	 KMeans kMeans= new KMeans(input2, 2);
	 System.out.println(kMeans.centroids()[0].length);
	 System.out.println(kMeans.getClusterLabel().length);
	 System.out.println(kMeans.getClusterSize()[0]);
	 for(int i=0;i<kMeans.getClusterLabel().length;i++)
		 System.out.print(kMeans.getClusterLabel()[i]);
	 for(int i=0;i<names.size();i++) 
		 input[i] = imageProcessor.getContourfeatures(names.get(i));
	 kMeans= new KMeans(input2, 2);
	 System.out.println(kMeans.centroids()[0].length);
	 System.out.println(kMeans.getClusterLabel().length);
	 System.out.println(kMeans.getClusterSize()[0]);
	 for(int i=0;i<kMeans.getClusterLabel().length;i++)
		 System.out.print(kMeans.getClusterLabel()[i]);
	 
	}
}
