package com.app.repository.impl;

import com.app.exceptions.ExceptionCode;
import com.app.exceptions.MyException;
import com.app.model.Stock;
import com.app.repository.StockRepository;
import com.app.repository.generic.AbstractGenericRepository;
import com.app.repository.generic.DbConnection;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;

public class StockRepositoryImpl extends AbstractGenericRepository<Stock> implements StockRepository {

    @Override
    public Optional<Stock> findStockByProductAndShop(String productName, String shopName) {

        EntityManagerFactory entityManagerFactory = DbConnection.getInstance().getEntityManagerFactory();

        Optional<Stock> optionalStock = Optional.empty();
        EntityManager entityManager = null;
        EntityTransaction tx = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            tx = entityManager.getTransaction();

            tx.begin();

            optionalStock = entityManager
                    .createQuery("select s from Stock s where s.product.name = :productName AND s.shop.name = :shopName", Stock.class)
                    .setParameter("productName", productName)
                    .setParameter("shopName", shopName)
                    .getResultList()
                    .stream()
                    .findFirst();

            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new MyException(ExceptionCode.STOCK, "STOCK FIND BY PRODUCT AND SHOP EXCEPTION ");
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
        return optionalStock;
    }

    @Override
    public int countProduct(Long productId) {
        EntityManagerFactory entityManagerFactory = DbConnection.getInstance().getEntityManagerFactory();
        EntityManager entityManager = null;
        EntityTransaction tx = null;
        int counter = 0;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            tx = entityManager.getTransaction();

            tx.begin();

            counter = entityManager
                    .createQuery("SELECT SUM( s.quantity ) FROM Stock s where s.product.id = :id", Long.class)
                    .setParameter("id", productId)
                    .getSingleResult()
                    .intValue();

            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
            throw new MyException(ExceptionCode.STOCK, "SUM PRODUCT EXCEPTION ");
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
        return counter;
    }

}
