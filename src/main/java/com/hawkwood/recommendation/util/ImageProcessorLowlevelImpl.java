package com.hawkwood.recommendation.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.LRUMap;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.stereotype.Component;

@Component
public class ImageProcessorLowlevelImpl implements ImageProcessor{
	
	private Map<String,Mat> matmap;
	private Map<String,Mat[]> HSVmap;
	public ImageProcessorLowlevelImpl(int cacheSize) {
		matmap = new LRUMap<String, Mat>(cacheSize);
	}
	public ImageProcessorLowlevelImpl() {
		matmap = new LRUMap<String, Mat>(500);
		HSVmap = new LRUMap<String, Mat[]>(500);
	}
	private Mat readImage(String filename) {
		Mat image = Imgcodecs.imread(filename);
		matmap.put(filename, image);
		return image;
	}
	
	@SuppressWarnings("unchecked")
	private Mat[] calculateHSV(Mat image, String name){
		
	    Imgproc.cvtColor(image, image, Imgproc.COLOR_RGB2HSV);

	    List<Mat> hsv_planes = new ArrayList<Mat>();
	    Core.split(image, hsv_planes);

	    MatOfInt histSize = new MatOfInt(256);

	    final MatOfFloat histRange = new MatOfFloat(0f, 256f);

	    boolean accumulate = false;

	    Mat h_hist = new Mat();
	    Mat s_hist = new Mat();
	    Mat v_hist = new Mat();

	    //error appear in the following sentences
        List<Mat> hList = new ArrayList<Mat>();
        hList.add(hsv_planes.get(0));
        List<Mat> sList = new ArrayList<Mat>();
        sList.add(hsv_planes.get(1));
        List<Mat> vList = new ArrayList<Mat>();
        vList.add(hsv_planes.get(2));
	    Imgproc.calcHist(hList, new MatOfInt(0), new Mat(), h_hist, histSize, histRange, accumulate);
	    Imgproc.calcHist(sList, new MatOfInt(0), new Mat(), s_hist, histSize, histRange, accumulate);
	    Imgproc.calcHist(vList, new MatOfInt(0), new Mat(), v_hist, histSize, histRange, accumulate);

	    int hist_w = 512;
	    int hist_h = 600;
	    //bin_w = Math.round((double) (hist_w / 256));

	    Mat histImage = new Mat(hist_h, hist_w, CvType.CV_8UC1);
	    Core.normalize(h_hist, h_hist, 3, histImage.rows(), Core.NORM_MINMAX);
	    Core.normalize(s_hist, s_hist, 3, histImage.rows(), Core.NORM_MINMAX);
	    Core.normalize(v_hist, v_hist, 3, histImage.rows(), Core.NORM_MINMAX);
	    Mat[] result = new Mat[]{h_hist, s_hist, v_hist} ;
	    HSVmap.put(name, result);
		return result;

    }

	private Double[] compareHSV(Mat[] hist1, Mat[] hist2, int method){
		if(hist1.length != hist2.length) throw new RuntimeException("input are not in same HSV space size");
		Double[] result = new Double[hist1.length];
		for(int i=0;i<result.length;++i)
			result[i] =  Imgproc.compareHist(hist1[i], hist2[i], method);;
		return result;
		
	}
	
	//CV_COMP_CORREL Correlation 0
	//CV_COMP_CHISQR Chi-Square 1 (Texture)
	//[1] Ahonen, T., Hadid, A., & Pietikäinen, M. (2004). Face recognition with local binary patterns. In Computer vision-eccv 2004 (pp. 469-481). Springer Berlin Heidelberg.
	//[2] Shan, C., Gong, S., & McOwan, P. W. (2009). Facial expression recognition based on local binary patterns: A comprehensive study. Image and Vision Computing, 27(6), 803-816.
	//CV_COMP_INTERSECT Intersection 2
	//CV_COMP_BHATTACHARYYA Bhattacharyya distance 3
	//CV_COMP_HELLINGER Synonym for CV_COMP_BHATTACHARYYA 4
	public List<Double> getfeaturesComparsion(String img1, String img2, int HSVmethod) {
		// TODO Auto-generated method stub
		List<Double> comparsionvector= new ArrayList<Double>();
		try {
			Mat image1 = matmap.getOrDefault(img1, readImage(img1));
			Mat image2 = matmap.getOrDefault(img2, readImage(img2));
			Double[] hsvComparsion = compareHSV(HSVmap.getOrDefault(img1, calculateHSV(image1, img1)), 
												HSVmap.getOrDefault(img2, calculateHSV(image2, img2)),
												HSVmethod);
			for(int i=0;i<hsvComparsion.length;i++) comparsionvector.add(hsvComparsion[i]);
		}catch (Exception e) {
		    e.printStackTrace();
		}
		return comparsionvector;
	}
}
