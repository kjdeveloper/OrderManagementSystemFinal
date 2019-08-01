package com.app.repository.impl;

import com.app.dto.ShopDto;
import com.app.exceptions.MyException;
import com.app.model.Shop;
import com.app.repository.ShopRepository;
import com.app.repository.generic.AbstractGenericRepository;
import com.app.repository.generic.DbConnection;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.Optional;

public class ShopRepositoryImpl extends AbstractGenericRepository<Shop> implements ShopRepository {
    @Override
    public Optional<Shop> findByName(ShopDto shopDto) {
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
                    .setParameter("name", shopDto.getName())
                    .getResultList()
                    .stream()
                    .findFirst();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new MyException("SHOP FIND BY NAME EXCEPTION ");
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }

        return optionalShop;
    }

    @Override
    public boolean isExistByShopAndCountry(ShopDto shopDTO) {
        EntityManagerFactory entityManagerFactory = DbConnection.getInstance().getEntityManagerFactory();

        boolean exist;
        EntityManager entityManager = null;
        EntityTransaction tx = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            tx = entityManager.getTransaction();

            tx.begin();

            exist = entityManager
                    .createQuery("select s from Shop s where s.name = :name AND s.country.name = :countryName", Shop.class)
                    .setParameter("name", shopDTO.getName())
                    .setParameter("countryName", shopDTO.getCountryDTO().getName())
                    .getResultList()
                    .stream()
                    .findFirst()
                    .isPresent();

            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new MyException("SHOP FIND BY NAME EXCEPTION ");
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
