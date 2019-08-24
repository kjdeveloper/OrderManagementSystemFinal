package com.app.repository.impl;

import com.app.dto.StockDto;
import com.app.exceptions.MyException;
import com.app.model.Stock;
import com.app.repository.StockRepository;
import com.app.repository.generic.AbstractGenericRepository;
import com.app.repository.generic.DbConnection;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
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
            throw new MyException("STOCK FIND BY PRODUCT AND SHOP EXCEPTION ");
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
                    .createQuery("select sum( s.quantity ) from Stock s where s.product.id = :id", Integer.class)
                    .setParameter("id", productId)
                    .getSingleResult();

            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new MyException("COUNT PRODUCT EXCEPTION ");
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
        return counter;
    }
}
