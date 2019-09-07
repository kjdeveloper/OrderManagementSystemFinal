package com.app.repository.impl;

import com.app.dto.ProducerDto;
import com.app.exceptions.ExceptionCode;
import com.app.exceptions.MyException;
import com.app.model.Producer;
import com.app.repository.ProducerRepository;
import com.app.repository.generic.AbstractGenericRepository;
import com.app.repository.generic.DbConnection;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class ProducerRepositoryImpl extends AbstractGenericRepository<Producer> implements ProducerRepository {
    @Override
    public Optional<Producer> findByName(String producerName) {
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
                    .setParameter("name", producerName)
                    .getResultList()
                    .stream()
                    .findFirst();

            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new MyException(ExceptionCode.PRODUCER, "PRODUCER FIND BY NAME EXCEPTION ");
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

        boolean exist = false;
        EntityManager entityManager = null;
        EntityTransaction tx = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            tx = entityManager.getTransaction();

            tx.begin();

            exist = entityManager
                    .createQuery("select p from Producer p where p.name = :name AND p.trade.name = :tradeName AND p.country.name = :countryName", Producer.class)
                    .setParameter("name", producerDto.getName())
                    .setParameter("tradeName", producerDto.getTradeDto().getName())
                    .setParameter("countryName", producerDto.getCountryDto().getName())
                    .getResultList()
                    .stream()
                    .findFirst()
                    .isPresent();

            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new MyException(ExceptionCode.PRODUCER, "PRODUCER FIND BY NAME EXCEPTION ");
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
    public Optional<Producer> findByNameAndCountry(ProducerDto producerDto) {
        EntityManagerFactory entityManagerFactory = DbConnection.getInstance().getEntityManagerFactory();

        Optional<Producer> producer = null;
        EntityManager entityManager = null;
        EntityTransaction tx = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            tx = entityManager.getTransaction();

            tx.begin();

            producer = entityManager
                    .createQuery("select p from Producer p where p.name = :name AND p.country.name = :countryName", Producer.class)
                    .setParameter("name", producerDto.getName())
                    .setParameter("countryName", producerDto.getCountryDto().getName())
                    .getResultList()
                    .stream()
                    .findFirst();

            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new MyException(ExceptionCode.PRODUCER, "PRODUCER FIND BY NAME AND COUNTRY EXCEPTION ");
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }

        return producer;
    }

    @Override
    public List<Producer> findProducerWithGivenBrandAndTheBiggerQuantityProducedThanGiven(String tradeName, Long quantity) {
        EntityManagerFactory entityManagerFactory = DbConnection.getInstance().getEntityManagerFactory();

        List<Producer> producers = null;
        EntityManager entityManager = null;
        EntityTransaction tx = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            tx = entityManager.getTransaction();

            tx.begin();

            producers = entityManager
                    .createQuery("SELECT p " +
                            "FROM Producer p " +
                            "WHERE p.trade.name = :tradeName " +
                            "AND size(p.products) > :quantity", Producer.class)
                    .setParameter("tradeName", tradeName)
                    .setParameter("quantity", quantity)
                    .getResultList();

            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new MyException(ExceptionCode.PRODUCER, "PRODUCER WITH GIVEN BRAND AND BIGGER QUANTITY EXCEPTION ");
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }

        return producers;
    }
}
