package com.hawkwood.recommendation.util;

import java.util.List;


public interface ImageProcessor {
	List<List<Double>> getfeaturesComparsion(String img1, String img2, int HSVmethod);
	List<String> imageNames(String path);
	double[] getHSVfeatures(String img);
}
