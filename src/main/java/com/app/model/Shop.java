package com.app.model;


import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "shops")
public class Shop {

    @Id
    @GeneratedValue
    private Long id;
    private String name;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "country_id")
    private Country country;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "shop")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Stock> stocks = new HashSet<>();

}
