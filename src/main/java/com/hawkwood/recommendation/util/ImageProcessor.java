package com.hawkwood.recommendation.util;

import java.util.List;


public interface ImageProcessor {
	public List<List<Double>> getfeaturesComparsion(String img1, String img2, int HSVmethod);
	public List<String> imageNames(String path);
}
