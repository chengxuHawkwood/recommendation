package com.hawkwood.recommendation.dao;

import com.hawkwood.recommendation.entity.SiftNode;

public interface SiftIndexNodeDao {

	SiftNode findById(String id);

	void saveSiftNode(SiftNode siftNode);

}
