package com.hawkwood.recommendation.util;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.LRUMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hawkwood.recommendation.dao.SiftIndexNodeDao;
import com.hawkwood.recommendation.dao.SiftIndexNodeDaoImpl;
import com.hawkwood.recommendation.entity.IndexNode;
import com.hawkwood.recommendation.entity.SiftNode;

import smile.clustering.KMeans;
@Service
public class SiftBinaryTreeImpl implements TreeBuilder{
	@Autowired
	ImageProcessor imageProcessor;
	@Autowired
	SiftIndexNodeDao siftIndexNodeDao;
	Map<Integer, Integer> level = new HashMap<>();
	LRUMap<String, double[]> centroids = new LRUMap<>();
	ThreadLocal<double[]> features = new ThreadLocal<>();
	public void BuildTree(String path){
		 level = new HashMap<Integer, Integer>();
		 List<String> names = imageProcessor.imageNames(path);
		 double[] testsizeInput = imageProcessor.getContourfeatures(names.get(0));
		 double[][] input = new double[names.size()][testsizeInput.length];
		 String[] inputnames = new String[names.size()];
		 for(int i=0;i<names.size();i++) {
			 input[i] = imageProcessor.getContourfeatures(names.get(i));
			 inputnames[i] = names.get(i); 
		 }
		 build("1.1", input, inputnames, null);
	}
	//true =left false=right
	
	boolean LeftBigger(double[] left, double[] right) {
		double leftL2 = 0, rightL2 =0 ;
		for(int i=0;i<left.length;i++) {
			leftL2 = Math.pow(left[i]-features.get()[i], 2);
			rightL2 = Math.pow(right[i]-features.get()[i], 2);
		}
		double d=0;
		if(leftL2>rightL2) return true;
		else return false;
				
	}
	
	
	//to search query(1.1, number, path, null)
	public String QueryPictures(String id, int number, String querypath, double[] query) {
		if(query == null) { 
			query = imageProcessor.getContourfeatures(querypath);
			features.set(query);
		}
		SiftNode indexNode = siftIndexNodeDao.findById(id);
		if(indexNode.getSize()<=2*number) return indexNode.getImagenames();
		SiftNode left =siftIndexNodeDao.findById(indexNode.getLeftchild());
		SiftNode right = siftIndexNodeDao.findById(indexNode.getRightchild());
		String leftcenname = left.getFeatures();
		String rightcenname = right.getFeatures();
		try {		
			
			double[] leftcen = null;
			double[] rightcen = null;
			ObjectMapper mapper = new ObjectMapper();
			if(centroids.containsKey(left.getId())){
				leftcen =centroids.get(left.getId());
			}else {
				File leftjson = new File(leftcenname);
				leftcen = mapper.readValue(leftjson, double[].class);
				centroids.put(left.getId(), leftcen);
			}
			if(centroids.containsKey(right.getId())){
				rightcen =centroids.get(right.getId());
			}else {
				File rightjson = new File(rightcenname);
				rightcen = mapper.readValue(rightjson, double[].class);
				centroids.put(right.getId(), rightcen);
			}			
			if(LeftBigger(leftcen, rightcen)) {
				return QueryPictures(right.getId(), number, querypath, query);
			}else {
				return QueryPictures(left.getId(),  number, querypath,  query);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			features.remove();
		}
		return indexNode.getImagenames();
	} 
	private void build(String val, double[][] input, String[] names, double[] centroids) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(new File("./resource/Sift/"+String.valueOf(val)+".centroids"), centroids);
			mapper.writeValue(new File("./resource/Sift/"+String.valueOf(val)+".names"), names);

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
		siftIndexNodeDao.saveSiftNode(new SiftNode(val, "./resource/Sift/"+String.valueOf(val)+".centroids", "./resource/Sift/"+String.valueOf(val)+".names", names.length, 
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
		double[] leftcen = kMeans.centroids()[0];
		double[] rightcen = kMeans.centroids()[1];
		if(rightnames.length==0 || leftnames.length==0) return;
		build(String.valueOf(Integer.parseInt(val.split("\\.")[0])+1)+"."+levelnumber1,rightinput, rightnames, leftcen);
		build(String.valueOf(Integer.parseInt(val.split("\\.")[0])+1)+"."+levelnumber2, leftinput, leftnames, rightcen);
	}
}
