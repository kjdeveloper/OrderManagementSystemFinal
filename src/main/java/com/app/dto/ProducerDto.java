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
public class ProducerDto {

    private Long id;
    private String name;
    private Set<ProductDto> productsDtos;
    private CountryDto countryDto;
    private TradeDto tradeDto;

}
