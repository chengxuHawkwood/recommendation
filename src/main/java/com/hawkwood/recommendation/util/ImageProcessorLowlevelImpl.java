package com.hawkwood.recommendation.util;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import javax.persistence.criteria.From;

import org.apache.commons.collections4.map.LRUMap;
import org.hibernate.dialect.identity.Ingres10IdentityColumnSupport;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.xfeatures2d.SIFT;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import smile.math.DoubleArrayList;

@Service
public class ImageProcessorLowlevelImpl implements ImageProcessor{
	
	private Map<String,Mat> matmap;
	private Map<String,Mat[]> HSVmap;
	private Map<String,Mat[]> TextureMap;
    int hBins = 20;
    int sBins = 20;
	private double[] max = new double[hBins*sBins];
	private double[] min = new double[hBins*sBins];
	
	public double[] getContourfeatures(String img1){
		Mat image1 = matmap.getOrDefault(img1, readImage(img1));
		Imgproc.resize(image1, image1, new Size(32,32));
		Imgproc.cvtColor(image1, image1, Imgproc.COLOR_RGB2GRAY);
	    Mat desc = new Mat();
	    MatOfKeyPoint keypoints = new MatOfKeyPoint();
	    SIFT.create().detectAndCompute(image1, new Mat(), keypoints, desc);
	    desc.convertTo(desc, CvType.CV_64F);
	    double[] result = new double[10000];
	    try {
		    desc.reshape(-1, 1);
		    desc.get(0, 0, result);
	    }catch (Exception e) {
			// TODO: handle exception
		}
		return result;

	}
	
 	public double[] getHSVfeatures(String img1) {
		Mat image1 = matmap.getOrDefault(img1, readImage(img1));
		Mat[] hsv= HSVmap.getOrDefault(img1, calculateHSVandTexture(image1, img1));
		float[] result = new float[(int) (hsv[0].total() * hsv[0].channels())];
		hsv[0].get(0, 0, result);
		double[] transedresult = new double[result.length]; 
		for(int i=0;i<result.length;i++)  transedresult[i] = ((double) result[i]);
		return normalize(transedresult);
	}
	public double[] normalize(double[] input) {
		double[] output = new double[hBins*sBins];
		for(int i=0;i<input.length;i++) output[i] = (input[i]-min[i])/(max[i]-min[i]);
		return output;
	}
	public ImageProcessorLowlevelImpl(int cacheSize) {
		matmap = new LRUMap<String, Mat>(cacheSize);
	}
	public ImageProcessorLowlevelImpl() {
		matmap = new LRUMap<String, Mat>(500);
		HSVmap = new LRUMap<String, Mat[]>(500);
		TextureMap = new LRUMap<String, Mat[]>(500);
		for(int i=0;i<min.length;i++) min[i] = Integer.MAX_VALUE;
		List<String> names = imageNames("/Users/hawkwood/Downloads/style-color-images/style/");
		for(String name:names) {
			Mat image1 = readImage(name);
			Mat[] hsv= calculateHSVandTexture(image1, name);
			float[] result = new float[(int) (hsv[0].total() * hsv[0].channels())];
			hsv[0].get(0, 0, result);
			for(int i=0;i<result.length;i++) {
				max[i] = Math.max(max[i], result[i]);
				min[i] = Math.min(min[i], result[i]);
			}
		}
	}
	private Mat readImage(String filename) {
		Mat image = Imgcodecs.imread(filename);
		matmap.put(filename, image);
		return image;
	}
	
	private Mat[] calculateHSVandTexture(Mat image, String name){
		
	    Imgproc.cvtColor(image, image, Imgproc.COLOR_RGB2HSV);

	    List<Mat> hsv_planes = new ArrayList<Mat>();
	    Core.split(image, hsv_planes);
	    MatOfInt histSize = new MatOfInt(hBins, sBins);

	    final MatOfFloat histRange = new MatOfFloat(0f, 180f, 0, 256f);

	    boolean accumulate = false;

	    Mat hs_hist = new Mat();


	    //error appear in the following sentences

	    Imgproc.calcHist(hsv_planes, new MatOfInt(0, 1), new Mat(), hs_hist, histSize, histRange, accumulate);
//
//	    Core.normalize(hs_hist, hs_hist, 0, hBins*sBins, Core.NORM_MINMAX);
	    Mat[] result = new Mat[]{hs_hist} ;
	    float[] hs_histData = new float[(int) (hs_hist.total() * hs_hist.channels())];
	    hs_hist.get(0, 0, hs_histData);
	    HSVmap.put(name, result);
	    TextureMap.put(name, result);
		return result;

    }

	private Double[] compareHSVorTexture(Mat[] hist1, Mat[] hist2, int method){
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
	public List<List<Double>> getfeaturesComparsion(String img1, String img2, int HSVmethod) {
		// TODO Auto-generated method stub
		List<List<Double>> comparsionResult = new ArrayList<List<Double>>();
		try {
			List<Double> comparsionvector= new ArrayList<Double>();
			Mat image1 = matmap.getOrDefault(img1, readImage(img1));
			Mat image2 = matmap.getOrDefault(img2, readImage(img2));
			Double[] hsvComparsion = compareHSVorTexture(HSVmap.getOrDefault(img1, calculateHSVandTexture(image1, img1)), 
												HSVmap.getOrDefault(img2, calculateHSVandTexture(image2, img2)),
												HSVmethod);
			for(int i=0;i<hsvComparsion.length;i++) comparsionvector.add(hsvComparsion[i]);
			comparsionResult.add(comparsionvector);
		}catch (Exception e) {
		    e.printStackTrace();
		}
		try {
			List<Double> comparsionvector= new ArrayList<Double>();
			Mat image1 = matmap.getOrDefault(img1, readImage(img1));
			Mat image2 = matmap.getOrDefault(img2, readImage(img2));
			Double[] TextureComparsion = compareHSVorTexture(HSVmap.getOrDefault(img1, calculateHSVandTexture(image1, img1)), 
					HSVmap.getOrDefault(img2, calculateHSVandTexture(image2, img2)),
				    1);
			for(int i=0;i<TextureComparsion.length;i++) comparsionvector.add(TextureComparsion[i]);
			comparsionResult.add(comparsionvector);
		}catch (Exception e) {
		    e.printStackTrace();
		}
		return comparsionResult;
	}

	public List<String> imageNames(String pathStr){
	    File path = new File(pathStr);  
	    File[] fileArr = path.listFiles();
	    List<String> names = new ArrayList<String>();
        for(int i=0;i<fileArr.length;i++) {
        	if(fileArr[i].getName().endsWith("png"))
        		names.add(fileArr[i].toString());
        }
		return names;
	}
}
