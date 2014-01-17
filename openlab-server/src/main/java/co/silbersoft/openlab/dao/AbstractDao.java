package co.silbersoft.openlab.dao;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.sql.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.util.ReflectionUtils;

public abstract class AbstractDao<T extends Object> implements Dao<T> {

    private SessionFactory sessionFactory;
    private Class<T> domainClass;

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    @SuppressWarnings("unchecked")
    private Class<T> getDomainClass() {
        if (domainClass == null) {
            ParameterizedType thisType = (ParameterizedType) getClass().getGenericSuperclass();
            this.domainClass = (Class<T>) thisType.getActualTypeArguments()[0];
        }
        return domainClass;
    }

    private String getDomainClassName() {
        return getDomainClass().getName();
    }

    @Override
    public void create(T t) {
        Method method = ReflectionUtils.findMethod(getDomainClass(), "setCreationDate", new Class[]{Date.class});
        if (method != null) {
            try {
                method.invoke(t, new Date(System.currentTimeMillis()));
            } catch (Exception e) {
            }
        }
        getSession().save(t);
    }

    @SuppressWarnings("unchecked")
    @Override
    public T get(Serializable id) {
        return (T) getSession().get(getDomainClass(), id);
    }

    @Override
    public T load(Serializable id) {
        return (T) getSession().load(getDomainClass(), id);
    }

    @Override
    public List<T> getAll() {
        return getSession().createQuery("from " + getDomainClassName()).list();
    }

    @Override
    public void update(T t) {
        getSession().update(t);
    }

    @Override
    public void delete(T t) {
        getSession().delete(t);
    }

    @Override
    public void deleteById(Serializable id) {
        delete(load(id));
    }

    public void deleteAll() {
        getSession().createQuery("delete " + getDomainClassName()).executeUpdate();
    }

    @Override
    public long count() {
        return (Long) getSession().createQuery("select count(*) from " + getDomainClassName()).uniqueResult();
    }

    @Override
    public boolean exists(Serializable id) {
        return (get(id) != null);
    }

    @Override
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}

