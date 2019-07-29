package com.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockDto {

    private Long id;
    private int quantity;
    private ProductDto productDTO;
    private ShopDto shopDTO;

}
