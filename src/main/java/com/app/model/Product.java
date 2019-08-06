package com.app.model;


import com.app.model.enums.EGuarantee;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "products")
        public class Product {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @Column(columnDefinition = "DECIMAL(19,2)")
    private BigDecimal price;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "product")
    private Set<CustomerOrder> customer_orders = new HashSet<>();

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "producer_id")
    private Producer producer;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "product")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Stock> stocks = new HashSet<>();

    @ElementCollection
    @CollectionTable(
            name = "guarantees",
            joinColumns = @JoinColumn(name = "guarantee_components_id")
    )
    @Column(name = "eguarantee")
    @Enumerated(EnumType.STRING)
   private Set<EGuarantee> eGuarantees = new HashSet<>();

}
