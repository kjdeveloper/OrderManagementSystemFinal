package com.app.repository.impl;

import com.app.exceptions.MyException;
import com.app.model.CustomerOrder;
import com.app.repository.CustomerOrderRepository;
import com.app.repository.generic.AbstractGenericRepository;
import com.app.repository.generic.DbConnection;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class CustomerOrderRepositoryImpl extends AbstractGenericRepository<CustomerOrder> implements CustomerOrderRepository {


    @Override
    public List<CustomerOrder> findOrdersBetweenDatesAndGivenPrice(LocalDateTime dateFrom, LocalDateTime dateTo, BigDecimal price) {

        if (dateFrom == null) {
            throw new MyException("START DATE CAN NOT BE NULL");
        }
        if (dateTo == null) {
            throw new MyException("FINISH DATE CAN NOT BE NULL");
        }
        if (dateFrom.compareTo(dateTo) >= 0) {
            throw new MyException("START DATE CAN NOT BE AFTER FINISH DATE");
        }
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new MyException("PRICE CAN NOT BE NULL, LESS OR EQUAL ZERO");
        }

        EntityManagerFactory entityManagerFactory = DbConnection.getInstance().getEntityManagerFactory();

        Timestamp dateFromDb = Timestamp.valueOf(dateFrom);
        Timestamp dateToDb = Timestamp.valueOf(dateTo);

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
            e.printStackTrace();
            throw new MyException("CUSTOMER ORDER EXCEPTION ");
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }

        return customerOrders;
    }
}
