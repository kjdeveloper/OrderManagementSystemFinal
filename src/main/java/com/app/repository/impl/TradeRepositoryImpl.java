package com.app.repository.impl;

import com.app.dto.TradeDto;
import com.app.exceptions.MyException;
import com.app.model.Trade;
import com.app.repository.TradeRepository;
import com.app.repository.generic.AbstractGenericRepository;
import com.app.repository.generic.DbConnection;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.Optional;

public class TradeRepositoryImpl extends AbstractGenericRepository<Trade> implements TradeRepository {
    @Override
    public Optional<Trade> findByName(TradeDto tradeDto) {
        EntityManagerFactory entityManagerFactory = DbConnection.getInstance().getEntityManagerFactory();

        Optional<Trade> optionalTrade = Optional.empty();
        EntityManager entityManager = null;
        EntityTransaction tx = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            tx = entityManager.getTransaction();

            tx.begin();

            optionalTrade = entityManager
                    .createQuery("select t from Trade t where t.name = :name", Trade.class)
                    .setParameter("name", tradeDto.getName())
                    .getResultList()
                    .stream()
                    .findFirst();

            tx.commit();
        }catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new MyException("TRADE FIND BY NAME EXCEPTION");
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }

        return optionalTrade;
    }
}
