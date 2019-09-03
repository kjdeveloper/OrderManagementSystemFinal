package com.app.repository.impl;

import com.app.exceptions.ExceptionCode;
import com.app.exceptions.MyException;
import com.app.model.Shop;
import com.app.repository.ShopRepository;
import com.app.repository.generic.AbstractGenericRepository;
import com.app.repository.generic.DbConnection;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;

public class ShopRepositoryImpl extends AbstractGenericRepository<Shop> implements ShopRepository {
    @Override
    public Optional<Shop> findByName(String shopName) {
        EntityManagerFactory entityManagerFactory = DbConnection.getInstance().getEntityManagerFactory();

        Optional<Shop> optionalShop = Optional.empty();
        EntityManager entityManager = null;
        EntityTransaction tx = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            tx = entityManager.getTransaction();

            tx.begin();

            optionalShop = entityManager
                    .createQuery("select s from Shop s where s.name = :name", Shop.class)
                    .setParameter("name", shopName)
                    .getResultList()
                    .stream()
                    .findFirst();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new MyException(ExceptionCode.SHOP, "SHOP FIND BY NAME EXCEPTION ");
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }

        return optionalShop;
    }

    @Override
    public boolean isExistByShopAndCountry(String shopName, String countryName) {
        EntityManagerFactory entityManagerFactory = DbConnection.getInstance().getEntityManagerFactory();

        boolean exist = false;
        EntityManager entityManager = null;
        EntityTransaction tx = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            tx = entityManager.getTransaction();

            tx.begin();

            exist = entityManager
                    .createQuery("select s from Shop s where s.name = :name AND s.country.name = :countryName", Shop.class)
                    .setParameter("name", shopName)
                    .setParameter("countryName", countryName)
                    .getResultList()
                    .stream()
                    .findFirst()
                    .isPresent();

            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new MyException(ExceptionCode.SHOP, "SHOP FIND BY NAME EXCEPTION ");
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

    @Override
    public List<Shop> findAllShopsWithProductsWithCountryDifferentThanShopCountry() {
        EntityManagerFactory entityManagerFactory = DbConnection.getInstance().getEntityManagerFactory();

        List<Shop> shops = null;
        EntityManager entityManager = null;
        EntityTransaction tx = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            tx = entityManager.getTransaction();

            tx.begin();

            shops = entityManager
                    .createQuery("select s, st " +
                            "from Shop s " +
                            "LEFT JOIN s.stocks st", Shop.class)
                    .getResultList();

            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
            throw new MyException(ExceptionCode.SHOP, "SHOP FIND ALL WITH DIFFERENT COUNTRY EXCEPTION ");
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }

        return shops;
    }
}
