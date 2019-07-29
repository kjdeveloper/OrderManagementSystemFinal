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
public class CountryDto {

    private Long id;
    private String name;
    private Set<ShopDto> shopDtos;
    private Set<CustomerDto> customerDtos;
    private Set<ProducerDto> producerDtos;

}
