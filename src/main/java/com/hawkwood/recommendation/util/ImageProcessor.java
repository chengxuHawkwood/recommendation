package com.hawkwood.recommendation.util;

import java.util.List;


public interface ImageProcessor {
	public List<Double> getfeaturesComparsion(String img1, String img2, int HSVmethod);
}
