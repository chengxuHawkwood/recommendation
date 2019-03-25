package com.hawkwood.recommendation.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "indexNode")
public class IndexNode {
	@Id
	@Column(name = "id")
	private int id;
	@Column(name = "hsvfeatures")
	private String hsvfeatures;
	@Column(name = "imagenames")
	private String imagenames;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getHsvfeatures() {
		return hsvfeatures;
	}
	public void setHsvfeatures(String hsvfeatures) {
		this.hsvfeatures = hsvfeatures;
	}
	public String getImagenames() {
		return imagenames;
	}
	public void setImagenames(String imagenames) {
		this.imagenames = imagenames;
	}
	public IndexNode(int id,  String hsvfeatures, String imagenames) {
		super();
		this.id = id;
		this.hsvfeatures = hsvfeatures;
	}

}
