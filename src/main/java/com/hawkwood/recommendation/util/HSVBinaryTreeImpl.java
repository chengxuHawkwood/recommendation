package com.hawkwood.recommendation.util;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hawkwood.recommendation.dao.IndexNodeDao;
import com.hawkwood.recommendation.entity.IndexNode;

import smile.clustering.KMeans;

@Component
public class HSVBinaryTreeImpl implements TreeBuilder {
	@Autowired
	ImageProcessor imageProcessor;
	@Autowired
	IndexNodeDao indexNodeDao;
	Map<Integer, Integer> level;
	public void BuildTree(String path){
	 level = new HashMap<Integer, Integer>();
	 List<String> names = imageProcessor.imageNames(path);
	 double[] testsizeInput = imageProcessor.getHSVfeatures(names.get(0));
	 double[][] input = new double[names.size()][testsizeInput.length];
	 String[] inputnames = new String[names.size()];
	 for(int i=0;i<names.size();i++) {
		 input[i] = imageProcessor.getHSVfeatures(names.get(i));
		 inputnames[i] = names.get(i); 
	 }

		build("1.1", input, inputnames, null);

	 
	}

	private void build(String val, double[][] input, String[] names, double[] centroids) {
		if(names.length<3) return;
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(new File("./resource/"+String.valueOf(val)+".centroids"), centroids);
			mapper.writeValue(new File("./resource/"+String.valueOf(val)+".names"), names);

		} catch (Exception e) {
			e.printStackTrace();
		}
		String features = "";
		if(centroids!=null && centroids.length!=0) {
			for(int i=0;i<centroids.length;i++)
				features = features+" "+centroids[i];
		}
		int levelnumber1 = level.getOrDefault(Integer.parseInt(val.split("\\.")[0])+1, 0)+1;
		level.put(Integer.parseInt(val.split("\\.")[0])+1, levelnumber1);
		int levelnumber2 = level.getOrDefault(Integer.parseInt(val.split("\\.")[0])+1, 0)+1;
		level.put(Integer.parseInt(val.split("\\.")[0])+1, levelnumber2);
		indexNodeDao.saveIndexNode(new IndexNode(val, "./resource/"+String.valueOf(val)+".centroids", "./resource/"+String.valueOf(val)+".names", names.length, 
									String.valueOf(Integer.parseInt(val.split("\\.")[0])+1)+"."+levelnumber1, 
									String.valueOf(Integer.parseInt(val.split("\\.")[0])+1)+"."+levelnumber2));
		if(input.length<=3) {
			return;
		}
		System.out.print(input.length+" "+input);
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
		centroids = null;
		double[] leftcen = kMeans.centroids()[0];
		double[] rightcen = kMeans.centroids()[1];
		kMeans = null;
		build(String.valueOf(Integer.parseInt(val.split("\\.")[0])+1)+"."+levelnumber1,rightinput, rightnames, leftcen);
		leftcen = null;
		build(String.valueOf(Integer.parseInt(val.split("\\.")[0])+1)+"."+levelnumber2, leftinput, leftnames, rightcen);
	}
}
