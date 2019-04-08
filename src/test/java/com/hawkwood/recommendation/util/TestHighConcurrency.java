package com.hawkwood.recommendation.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class TestHighConcurrency {
	public static void main(String[] args) {
		for(int i=0;i<10000000;i++) {
			Thread thread = new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
					      StringBuilder result = new StringBuilder();
					      URL url = new URL("http://127.0.0.1:8080/query?path=/images/style/6_8_026.png");
					      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					      conn.setRequestMethod("GET");
					      BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
					      String line;
					      while ((line = rd.readLine()) != null) {
					         result.append(line);
					      }
					      rd.close();
					}catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}

				}
			});

		}
		System.out.println("successfully");
	}
}
