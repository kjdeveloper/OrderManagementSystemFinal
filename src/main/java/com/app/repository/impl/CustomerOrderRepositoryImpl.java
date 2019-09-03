package com.app.repository.impl;

import com.app.exceptions.ExceptionCode;
import com.app.exceptions.MyException;
import com.app.model.CustomerOrder;
import com.app.model.Product;
import com.app.repository.CustomerOrderRepository;
import com.app.repository.generic.AbstractGenericRepository;
import com.app.repository.generic.DbConnection;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class CustomerOrderRepositoryImpl extends AbstractGenericRepository<CustomerOrder> implements CustomerOrderRepository {


    @Override
    public List<CustomerOrder> findOrdersBetweenDatesAndGivenPrice(LocalDate dateFrom, LocalDate dateTo, BigDecimal price) {

        EntityManagerFactory entityManagerFactory = DbConnection.getInstance().getEntityManagerFactory();

        Date dateFromDb = Date.valueOf(dateFrom);
        Date dateToDb = Date.valueOf(dateTo);

        List<CustomerOrder> customerOrders = null;
        EntityManager entityManager = null;
        EntityTransaction tx = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            tx = entityManager.getTransaction();

            tx.begin();

            customerOrders = entityManager
                    .createQuery("SELECT cs " +
                            "FROM CustomerOrder  cs " +
                            "WHERE cs.date BETWEEN :dateFrom AND :dateTo " +
                            "AND (cs.product.price - (cs.product.price * cs.discount)) > :price", CustomerOrder.class)
                    .setParameter("dateFrom", dateFromDb)
                    .setParameter("dateTo", dateToDb)
                    .setParameter("price", price.doubleValue())
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

        return customerOrders;
    }

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
}
