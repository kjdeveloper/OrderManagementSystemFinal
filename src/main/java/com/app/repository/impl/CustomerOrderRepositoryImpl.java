package com.app.repository.impl;

import com.app.dto.CustomerDto;
import com.app.exceptions.ExceptionCode;
import com.app.exceptions.MyException;
import com.app.model.Customer;
import com.app.model.CustomerOrder;
import com.app.repository.CustomerOrderRepository;
import com.app.repository.generic.AbstractGenericRepository;
import com.app.repository.generic.DbConnection;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.List;

public class CustomerOrderRepositoryImpl extends AbstractGenericRepository<CustomerOrder> implements CustomerOrderRepository {

    @Override
    public List<CustomerOrder> findProductsByCustomerAndHisCountry(String customerName, String customerSurname, String countryName) {

        EntityManagerFactory entityManagerFactory = DbConnection.getInstance().getEntityManagerFactory();

        List<CustomerOrder> products = null;
        EntityManager entityManager = null;
        EntityTransaction tx = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            tx = entityManager.getTransaction();

            tx.begin();

            products = entityManager
                    .createQuery("SELECT cs " +
                            "FROM CustomerOrder cs " +
                            "WHERE cs.customer.name = :customerName " +
                            "AND cs.customer.surname = :customerSurname " +
                            "AND cs.customer.country.name = :countryName " +
                            "GROUP BY cs.product.producer", CustomerOrder.class)
                    .setParameter("customerName", customerName)
                    .setParameter("customerSurname", customerSurname)
                    .setParameter("countryName", countryName)
                    .getResultList();

            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new MyException(ExceptionCode.CUSTOMER_ORDER, "CUSTOMER ORDER EXCEPTION ");
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }

        return products;
    }

    @Override
    public List<Customer> findCustomersWhoOrderedProductWithSameCountryAsTheir() {

        EntityManagerFactory entityManagerFactory = DbConnection.getInstance().getEntityManagerFactory();

        List customers = null;
        EntityManager entityManager = null;
        EntityTransaction tx = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            tx = entityManager.getTransaction();

            tx.begin();

            customers = entityManager
                    .createQuery("SELECT customer FROM CustomerOrder customerOrder JOIN customerOrder.customer customer JOIN customer.country customerCountry " +
                            "JOIN customerOrder.product product JOIN product.producer producer JOIN producer.country producerCountry WHERE customerCountry.id = producerCountry.id")
                    .getResultList();

            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new MyException(ExceptionCode.CUSTOMER_ORDER, "CUSTOMER ORDER EXCEPTION ");
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
        return customers;
    }

    @Override
    public int findQuantityOfProductsOrderedWithDifferentCountryThanCustomer(Long id) {
        EntityManagerFactory entityManagerFactory = DbConnection.getInstance().getEntityManagerFactory();
        EntityManager entityManager = null;
        EntityTransaction tx = null;
        int counter = 0;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            tx = entityManager.getTransaction();

            tx.begin();

            counter = entityManager
                    .createQuery("select sum(co.quantity) from CustomerOrder co join co.product.producer.country prod_country join co.customer.country cust_country where co.customer.id = :id and prod_country.id != cust_country.id", Long.class)
                    .setParameter("id", id)
                    .getSingleResult()
                    .intValue();

            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
            throw new MyException(ExceptionCode.CUSTOMER_ORDER, "SUM PRODUCT EXCEPTION IN CUSTOMER ORDER");
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
        return counter;
    }
}
