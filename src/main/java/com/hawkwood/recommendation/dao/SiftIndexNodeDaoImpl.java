package com.hawkwood.recommendation.dao;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.hawkwood.recommendation.entity.SiftNode;
@Repository
public class SiftIndexNodeDaoImpl implements SiftIndexNodeDao{
	@Autowired
	private EntityManager  entityManager;
	SessionFactory sessionFactory;
	@Transactional
	public void saveSiftNode(SiftNode siftNode) {
		Session session  = entityManager.unwrap(Session.class);
		session.save(siftNode);
	}
	@Transactional
	public SiftNode findById(String id) {
		Session session  = entityManager.unwrap(Session.class);
		return session.load(SiftNode.class, id);
	}

}

