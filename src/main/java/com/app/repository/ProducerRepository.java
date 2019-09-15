package com.app.repository;

import com.app.dto.ProducerDto;
import com.app.model.Producer;
import com.app.repository.generic.GenericRepository;

import java.util.List;
import java.util.Optional;

public interface ProducerRepository extends GenericRepository<Producer> {
    Optional<Producer> findByName(String producerName);

    Optional<Producer> findByNameAndCountry(ProducerDto producerDto);

    boolean isExistByNameAndTradeAndCountry(ProducerDto producerDto);
}
