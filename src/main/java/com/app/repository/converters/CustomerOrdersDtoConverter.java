package com.app.repository.converters;

import com.app.dto.CustomerOrderDto;
import lombok.NoArgsConstructor;

import java.util.Collection;

@NoArgsConstructor
public class CustomerOrdersDtoConverter extends JsonConverter<Collection<CustomerOrderDto>> {
}
