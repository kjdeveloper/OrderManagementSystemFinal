package com.app.repository.impl;

import com.app.dto.ProductDto;
import com.app.exceptions.MyException;
import com.app.model.Product;
import com.app.repository.ProductRepository;
import com.app.repository.generic.AbstractGenericRepository;
import com.app.repository.generic.DbConnection;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.Optional;

public class ProductRepositoryImpl extends AbstractGenericRepository<Product> implements ProductRepository {

    @Override
    public Optional<Product> findByName(ProductDto productDto) {
        EntityManagerFactory entityManagerFactory = DbConnection.getInstance().getEntityManagerFactory();

        Optional<Product> optionalProduct = Optional.empty();
        EntityManager entityManager = null;
        EntityTransaction tx = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            tx = entityManager.getTransaction();

            tx.begin();

            optionalProduct = entityManager
                    .createQuery("select p from Product p where p.name = :name", Product.class)
                    .setParameter("name", productDto.getName())
                    .getResultList()
                    .stream()
                    .findFirst();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new MyException("PRODUCT FIND BY NAME EXCEPTION ");
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }

        return optionalProduct;
    }

    @Override
    public boolean isExistByNameAndCategoryAndProducer(ProductDto productDTO) {
        EntityManagerFactory entityManagerFactory = DbConnection.getInstance().getEntityManagerFactory();

        boolean exist;
        EntityManager entityManager = null;
        EntityTransaction tx = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            tx = entityManager.getTransaction();

            tx.begin();

            exist = entityManager
                    .createQuery("select p from Product p where p.name = :name AND p.category.name =:categoryName AND p.producer.name = :producerName", Product.class)
                    .setParameter("name", productDTO.getName())
                    .setParameter("categoryName", productDTO.getCategoryDto().getName())
                    .setParameter("producerName", productDTO.getProducerDto().getName())
                    .getResultList()
                    .stream()
                    .findFirst()
                    .isPresent();

            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new MyException("PRODUCT FIND BY METHOD = IS EXIST BY NAME AND CATEGORY AND PRODUCER EXCEPTION ");
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }

        return exist;
    }

}
