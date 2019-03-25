package com.hawkwood.recommendation.dao;

import javax.persistence.EntityManagerFactory;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import com.hawkwood.recommendation.entity.IndexNode;


@Repository
public class IndexNodeDaoImpl implements IndexNodeDao {
	@Autowired
	private EntityManagerFactory  entityManagerFactory;
	SessionFactory sessionFactory;
	@Transactional
	public void saveIndexNode(IndexNode indexNode) {
		Session session  = entityManagerFactory.unwrap(Session.class);
		session.save(indexNode);
	}
	@Transactional
	@Cacheable(value = "indexNodescache")
	public IndexNode findById(int id) {
		Session session  = entityManagerFactory.unwrap(Session.class);
		return session.load(IndexNode.class, id);
	}
}
