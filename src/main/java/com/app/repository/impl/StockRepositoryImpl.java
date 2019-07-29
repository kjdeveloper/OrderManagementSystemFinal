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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StockRepositoryImpl extends AbstractGenericRepository<Stock> implements StockRepository {
    @Override
    public List<Stock> findByQuantity(int quantity) {
        EntityManagerFactory entityManagerFactory = DbConnection.getInstance().getEntityManagerFactory();

        List<Stock> optionalStock = new ArrayList<>();
        EntityManager entityManager = null;
        EntityTransaction tx = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            tx = entityManager.getTransaction();

            tx.begin();

            optionalStock = new ArrayList<>(entityManager
                    .createQuery("select s from Stock s where s.quantity = :quantity", Stock.class)
                    .setParameter("quantity", quantity)
                    .getResultList());

            tx.commit();
        }catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new MyException("STOCK FIND BY NAME EXCEPTION ");
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }

        return optionalStock;
    }

    @Override
    public Optional<Stock> findStockByProductAndShop(StockDto stockDTO) {

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
                    .setParameter("productName", stockDTO.getProductDTO().getName())
                    .setParameter("shopName", stockDTO.getShopDTO().getName())
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
}
