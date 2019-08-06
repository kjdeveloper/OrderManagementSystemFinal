package com.app.model;

import com.app.model.enums.EPayment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "customers_orders")
public class CustomerOrder {

    @Id
    @GeneratedValue
    private Long id;
    private Timestamp date;
    @Column(columnDefinition = "DECIMAL(2,1)")
    private Double discount;
    private int quantity;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ElementCollection
    @CollectionTable(
            name = "payments",
            joinColumns = @JoinColumn(name = "payments_id")
    )
    @Column(name = "epayment")
    @Enumerated(EnumType.STRING)
    private Set<EPayment> ePayments = new HashSet<>();

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "product_id")
    private Product product;


}
