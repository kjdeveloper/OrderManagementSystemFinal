package com.app.repository.impl;

import com.app.dto.ProducerDto;
import com.app.exceptions.MyException;
import com.app.model.Producer;
import com.app.repository.ProducerRepository;
import com.app.repository.generic.AbstractGenericRepository;
import com.app.repository.generic.DbConnection;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.Optional;

public class ProducerRepositoryImpl extends AbstractGenericRepository<Producer> implements ProducerRepository {
    @Override
    public Optional<Producer> findByName(ProducerDto producerDto) {
        EntityManagerFactory entityManagerFactory = DbConnection.getInstance().getEntityManagerFactory();

        Optional<Producer> optionalProducer = Optional.empty();
        EntityManager entityManager = null;
        EntityTransaction tx = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            tx = entityManager.getTransaction();

            tx.begin();

            optionalProducer = entityManager
                    .createQuery("select p from Producer p where p.name = :name", Producer.class)
                    .setParameter("name", producerDto.getName())
                    .getResultList()
                    .stream()
                    .findFirst();

            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new MyException("PRODUCER FIND BY NAME EXCEPTION ");
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }

        return optionalProducer;
    }

    @Override
    public boolean isExistByNameAndTradeAndCountry(ProducerDto producerDto) {
        EntityManagerFactory entityManagerFactory = DbConnection.getInstance().getEntityManagerFactory();

        boolean exist;
        EntityManager entityManager = null;
        EntityTransaction tx = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            tx = entityManager.getTransaction();

            tx.begin();

            exist = entityManager
                    .createQuery("select p from Producer p where p.name = :name AND p.trade.name = :tradeName AND p.country.name = :countryName", Producer.class)
                    .setParameter("name", producerDto.getName())
                    .setParameter("tradeName", producerDto.getTradeDTO().getName())
                    .setParameter("countryName", producerDto.getCountryDTO().getName())
                    .getResultList()
                    .stream()
                    .findFirst()
                    .isPresent();

            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new MyException("PRODUCER FIND BY NAME EXCEPTION ");
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
