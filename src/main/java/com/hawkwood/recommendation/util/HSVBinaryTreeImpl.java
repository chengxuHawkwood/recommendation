package com.hawkwood.recommendation.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hawkwood.recommendation.dao.IndexNodeDao;
import com.hawkwood.recommendation.entity.IndexNode;

import smile.clustering.KMeans;

@Service
public class HSVBinaryTreeImpl implements TreeBuilder {
	@Autowired
	ImageProcessor imageProcessor;
	@Autowired
	IndexNodeDao indexNodeDao;

	public void BuildTree(String path){
	 List<String> names = imageProcessor.imageNames(path);
	 double[] testsizeInput = imageProcessor.getHSVfeatures(names.get(0));
	 double[][] input = new double[names.size()][testsizeInput.length];
	 String[] inputnames = new String[names.size()];
	 for(int i=0;i<names.size();i++) {
		 input[i] = imageProcessor.getHSVfeatures(names.get(i));
		 inputnames[i] = names.get(i); 
	 }
	 build(1, input, inputnames, null);
	 
	}

	private void build(int val, double[][] input, String[] names, double[] centroids) {
		String features = "";
		if(centroids.length!=0) {
			for(int i=0;i<centroids.length;i++)
				features = features+" "+centroids[i];
		}
		indexNodeDao.saveIndexNode(new IndexNode(val, features, String.join("  ", names)));
		if(input.length==1) {
			return;
		}
		KMeans kMeans = new KMeans(input, 2);
		double[][] leftinput = new double[kMeans.getClusterSize()[0]][input[0].length];
		String[] leftnames = new String[kMeans.getClusterSize()[0]];
		double[][] rightinput = new double[kMeans.getClusterSize()[1]][input[1].length];
		String[] rightnames = new String[kMeans.getClusterSize()[1]];
		int index1=0,index2=0;
		int[] results = kMeans.getClusterLabel();

		for(int i=0;i<input.length;i++) {
			if(results[i]==0) {
				leftinput[index1] = input[i];
				leftnames[index1] = names[i];
				index1++;
			}else {
				rightinput[index2] = input[i];
				rightnames[index2] = names[i];
				index2++;
			}			
		}
	
		input = null;
		names = null;
		build(2*val,rightinput, rightnames, kMeans.centroids()[0]);
		build(2*val+1, leftinput, leftnames, kMeans.centroids()[1]);
	}
}
