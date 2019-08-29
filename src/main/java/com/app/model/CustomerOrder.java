package com.app.model;

import com.app.model.enums.EPayment;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "customersOrders")
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

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "payments",
            joinColumns = @JoinColumn(name = "payments_id")
    )
    @Column(name = "epayment")
    @Enumerated(EnumType.STRING)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<EPayment> ePayments = new HashSet<>();

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "product_id")
    private Product product;


}
