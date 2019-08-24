package com.app.repository.impl;

import com.app.dto.CustomerDto;
import com.app.exceptions.MyException;
import com.app.model.Customer;
import com.app.repository.CustomerRepository;
import com.app.repository.generic.AbstractGenericRepository;
import com.app.repository.generic.DbConnection;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.Optional;

public class CustomerRepositoryImpl extends AbstractGenericRepository<Customer> implements CustomerRepository {
    @Override
    public Optional<Customer> findByName(String name) {
        EntityManagerFactory entityManagerFactory = DbConnection.getInstance().getEntityManagerFactory();

        Optional<Customer> optionalCustomer = Optional.empty();
        EntityManager entityManager = null;
        EntityTransaction tx = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            tx = entityManager.getTransaction();

            tx.begin();

            optionalCustomer = entityManager
                    .createQuery("select c from Customer c where c.name = :name", Customer.class)
                    .setParameter("name", name)
                    .getResultList()
                    .stream()
                    .findFirst();

            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new MyException("CUSTOMER FIND BY NAME EXCEPTION ");
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
        return optionalCustomer;
    }

    @Override
    public Optional<Customer> findBySurname(String surname) {
        EntityManagerFactory entityManagerFactory = DbConnection.getInstance().getEntityManagerFactory();

        Optional<Customer> optionalCustomer = Optional.empty();
        EntityManager entityManager = null;
        EntityTransaction tx = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            tx = entityManager.getTransaction();

            tx.begin();

            optionalCustomer = entityManager
                    .createQuery("select c from Customer c where c.surname = :surname", Customer.class)
                    .setParameter("surname", surname)
                    .getResultList()
                    .stream()
                    .findFirst();

            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new MyException("CUSTOMER FIND BY SURNAME EXCEPTION ");
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
        return optionalCustomer;
    }

    @Override
    public boolean isExistByNameAndSurnameAndCountry(CustomerDto customerDTO) {
        EntityManagerFactory entityManagerFactory = DbConnection.getInstance().getEntityManagerFactory();

        boolean exist = false;

        EntityManager entityManager = null;
        EntityTransaction tx = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            tx = entityManager.getTransaction();

            tx.begin();

            exist = entityManager
                    .createQuery("select c from Customer c where c.name = :name AND c.surname = :surname AND c.country.name = :country", Customer.class)
                    .setParameter("name", customerDTO.getName())
                    .setParameter("surname", customerDTO.getSurname())
                    .setParameter("country", customerDTO.getCountryDto().getName())
                    .getResultList()
                    .stream()
                    .findFirst()
                    .isPresent();

            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new MyException("CUSTOMER FIND BY NAME AND SURNAME AND COUNTRY EXCEPTION");
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
        if (exist) {
            return true;
        }
        return false;
    }
}
