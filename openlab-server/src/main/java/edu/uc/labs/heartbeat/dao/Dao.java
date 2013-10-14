package edu.uc.labs.heartbeat.dao;

import org.hibernate.SessionFactory;

import java.io.Serializable;
import java.util.List;

public interface Dao<T extends Object> {
	void create(T t);
	T get(Serializable id);
	T load(Serializable id);
	List<T> getAll();
	void update(T t);
	void delete(T t);
	void deleteById(Serializable id);
	long count();
	boolean exists(Serializable id);
	void setSessionFactory(SessionFactory sessionFactory);
}
