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
@Table(name = "countries")
public class Country {

    @Id
    @GeneratedValue
    private Long id;
    private String name;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "country")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Shop> shops = new HashSet<>();

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "country")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Customer> customers = new HashSet<>();

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "country")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Producer> producers = new HashSet<>();


}
