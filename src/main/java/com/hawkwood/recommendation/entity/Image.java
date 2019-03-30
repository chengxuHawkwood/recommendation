package com.hawkwood.recommendation.entity;

import java.util.List;

public class Image {
	String path;
	public List<String> show;
	public List<String> getShow() {
		return show;
	}

	public void setShow(List<String> show) {
		this.show = show;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
}
