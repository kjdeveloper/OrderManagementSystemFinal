package com.app.repository.converters;

import com.app.dto.CustomerDto;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;

@NoArgsConstructor
public class CustomersDtoConverter extends JsonConverter<Collection<CustomerDto>> {
}
