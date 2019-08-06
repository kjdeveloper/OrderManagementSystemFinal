package com.app.dto;

import com.app.model.enums.EPayment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerOrderDto {

    private Long id;
    private LocalDateTime date;
    private Double discount;
    private int quantity;
    private CustomerDto customerDto;
    private Set<EPayment> ePayments;
    private ProductDto productDto;

}
