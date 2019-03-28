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
	private String id;
	@Column(name = "hsvfeatures")
	private String hsvfeatures;
	@Column(name = "imagenames")
	private String imagenames;
	@Column(name = "size")
	private int size;
	@Column(name = "leftchild")
	private String leftchild;
	@Column(name = "rightchild")
	private String rightchild;
	public String getLeftchild() {
		return leftchild;
	}
	public void setLeftchild(String leftchild) {
		this.leftchild = leftchild;
	}
	public String getRightchild() {
		return rightchild;
	}
	public void setRightchild(String rightchild) {
		this.rightchild = rightchild;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
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
	public IndexNode(String val,  String hsvfeatures, String imagenames, int size, String leftchild, String rightchild) {
		super();
		this.id = val;
		this.hsvfeatures = hsvfeatures;
		this.imagenames = imagenames;
		this.size = size;
		this.leftchild =leftchild;
		this.rightchild =rightchild;
	}

}
