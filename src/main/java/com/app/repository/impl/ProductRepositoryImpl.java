package com.app.repository.impl;

import com.app.dto.ProductDto;
import com.app.exceptions.MyException;
import com.app.model.CustomerOrder;
import com.app.model.Product;
import com.app.model.enums.EGuarantee;
import com.app.repository.ProductRepository;
import com.app.repository.generic.AbstractGenericRepository;
import com.app.repository.generic.DbConnection;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ProductRepositoryImpl extends AbstractGenericRepository<Product> implements ProductRepository {

    @Override
    public Optional<Product> findByName(String productName) {
        if (productName == null){
            throw new MyException("PRODUCT NAME CAN NOT BE NULL");
        }
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
                    .setParameter("name", productName)
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
        if (productDTO == null){
            throw new MyException("PRODUCT CAN NOT BE NULL");
        }

        EntityManagerFactory entityManagerFactory = DbConnection.getInstance().getEntityManagerFactory();

        boolean exist = false;
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

    @Override
    public List<Product> findProductsWithBiggestPriceInCategory() {
        EntityManagerFactory entityManagerFactory = DbConnection.getInstance().getEntityManagerFactory();

        List<Product> products = null;
        EntityManager entityManager = null;
        EntityTransaction tx = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            tx = entityManager.getTransaction();

            tx.begin();

            products = entityManager
                    .createQuery("SELECT p " +
                            "FROM Product p " +
                            "GROUP BY p.category.name " +
                            "ORDER BY p.price DESC", Product.class)
                    .getResultList();

            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new MyException("PRODUCT WITH BIGGEST PRICE EXCEPTION");
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }

        return products;
    }

    @Override
    public List<Product> findAllProductsFromSpecificCountryBetweenCustomerAges(String countryName, int ageFrom, int ageTo) {
        EntityManagerFactory entityManagerFactory = DbConnection.getInstance().getEntityManagerFactory();
        List<Product> products = null;
        EntityManager entityManager = null;
        EntityTransaction tx = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            tx = entityManager.getTransaction();

            tx.begin();

            products = entityManager
                    .createQuery("SELECT cs " +
                            "FROM CustomerOrder cs " +
                            "WHERE cs.customer.country.name = :countryName " +
                            "AND cs.customer.age > :ageFrom " +
                            "AND cs.customer.age < :ageTo", CustomerOrder.class)
                    .setParameter("countryName", countryName)
                    .setParameter("ageFrom", ageFrom)
                    .setParameter("ageTo", ageTo)
                    .getResultList()
                    .stream()
                    .map(CustomerOrder::getProduct)
                    .collect(Collectors.toList());

            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new MyException("PRODUCT WITH SPECIFIED CUSTOMER EXCEPTION");
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
        return products;
    }

    @Override
    public List<Product> findAllProductsWithGivenGuarantees(Set<EGuarantee> eGuarantees) {
        EntityManagerFactory entityManagerFactory = DbConnection.getInstance().getEntityManagerFactory();

        List<Product> products = null;
        EntityManager entityManager = null;
        EntityTransaction tx = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            tx = entityManager.getTransaction();

            tx.begin();

            products = entityManager
                    .createQuery("SELECT p.eGuarantees " +
                            "FROM Product p " +
                            "WHERE :eGuarantees MEMBER OF p.eGuarantees", Product.class)
                    .setParameter("eGuarantees", eGuarantees)
                    .getResultList();

            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new MyException("PRODUCT WITH GUARANTEES EXCEPTION");
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }

        return products;
    }


}
