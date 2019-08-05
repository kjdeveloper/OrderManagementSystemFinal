package com.app.service.mainservicemethods;

import com.app.dto.ProductDto;
import com.app.exceptions.MyException;
import com.app.model.Product;
import com.app.repository.generic.DbConnection;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class DataDownloadService {

    private final EntityManagerFactory entityManagerFactory = DbConnection.getInstance().getEntityManagerFactory();

    public DataDownloadService() {
    }
/*
    public Map<BigDecimal, ProductDto> getProductsWithHighestPriceInEachCategory(){
        Map<BigDecimal, ProductDto> products = null;
        EntityManager entityManager = null;
        EntityTransaction tx = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            tx = entityManager.getTransaction();

            tx.begin();
            products = entityManager
                    .createQuery("")
            ;

            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new MyException("");
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }

        return products;
    }*/


}
