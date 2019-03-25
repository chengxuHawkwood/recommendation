package com.hawkwood.recommendation.dao;

import com.hawkwood.recommendation.entity.IndexNode;

public interface IndexNodeDao {

	public void saveIndexNode(IndexNode indexNode);
	public IndexNode findById(int id);
}
