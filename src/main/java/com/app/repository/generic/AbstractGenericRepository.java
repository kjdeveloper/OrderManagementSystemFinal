package com.app.repository.generic;

import com.app.exceptions.MyException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;

public abstract class AbstractGenericRepository<T> implements GenericRepository<T> {

    private EntityManagerFactory entityManagerFactory = DbConnection.getInstance().getEntityManagerFactory();
    private Class<T> entityClass = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    protected EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }

    @Override
    public Optional<T> addOrUpdate(T t) {
        Optional<T> op = Optional.empty();
        EntityManager entityManager = null;
        EntityTransaction tx = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            tx = entityManager.getTransaction();

            tx.begin();

            T inserted = entityManager.merge(t);
            op = Optional.of(inserted);

            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new MyException(entityClass.getCanonicalName() + ": ADD OR UPDATE EXCEPTION ");
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }

        return op;
    }

    @Override
    public Optional<T> findById(Long id) {
        Optional<T> op = Optional.empty();
        EntityManager entityManager = null;
        EntityTransaction tx = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            tx = entityManager.getTransaction();

            tx.begin();
            op = Optional.of(entityManager.find(entityClass, id));
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new MyException(entityClass.getCanonicalName() + ": FIND BY ID EXCEPTION ");
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }

        return op;
    }

    @Override
    public List<T> findAll() {
        List<T> items = null;
        EntityManager entityManager = null;
        EntityTransaction tx = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            tx = entityManager.getTransaction();

            tx.begin();

            items = entityManager
                    .createQuery("select t from " + entityClass.getCanonicalName() + " t", entityClass)
                    .getResultList();

            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new MyException(entityClass.getCanonicalName() + ": FIND ALL EXCEPTION ");
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }

        return items;
    }

    @Override
    public Optional<T> delete(Long id) {
        Optional<T> item = Optional.empty();
        EntityManager entityManager = null;
        EntityTransaction tx = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            tx = entityManager.getTransaction();

            tx.begin();

            item = Optional.of(entityManager.find(entityClass, id));
            entityManager.remove(item);

            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new MyException(entityClass.getCanonicalName() + ": ADD OR UPDATE EXCEPTION ");
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }

        return item;

    }

    @Override
    public List<T> deleteAll() {
        List<T> items = null;
        EntityManager entityManager = null;
        EntityTransaction tx = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            tx = entityManager.getTransaction();

            tx.begin();
            items = entityManager
                    .createQuery("select t from" + entityClass.getCanonicalName() + " t", entityClass)
                    .getResultList();
            for (T t : items) {
                entityManager.remove(t);
            }

            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new MyException(entityClass.getCanonicalName() + ": REMOVE ALL EXCEPTION ");
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
        return items;
    }

}
