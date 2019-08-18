package com.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerDto {

    private Long id;
    private int age;
    private String name;
    private String surname;
    private Set<CustomerOrderDto> customerOrderDtos;
    private CountryDto countryDto;

}
