package com.app.repository.impl;

import com.app.exceptions.Error;
import com.app.exceptions.ExceptionCode;
import com.app.exceptions.MyException;
import com.app.repository.ErrorRepository;
import com.app.repository.generic.AbstractGenericRepository;
import com.app.repository.generic.DbConnection;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.List;

public class ErrorRepositoryImpl extends AbstractGenericRepository<Error> implements ErrorRepository {

 /*   @Override
    public List<Error> getErrors() {
        EntityManagerFactory entityManagerFactory = DbConnection.getInstance().getEntityManagerFactory();

        List<Error> errors = null;

        EntityManager entityManager = null;
        EntityTransaction tx = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            tx = entityManager.getTransaction();

            tx.begin();

            errors = entityManager
                    .createQuery("select c from Customer c where c.name = :name AND c.surname = :surname AND c.country.name = :country", Error.class)
                    .getResultList();

            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new MyException(ExceptionCode.ERROR, "ERRORS EXCEPTION");
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
        return errors;
    }*/
}
