package com.app.repository;

import com.app.dto.TradeDto;
import com.app.model.Trade;
import com.app.repository.generic.GenericRepository;

import java.util.Optional;

public interface TradeRepository extends GenericRepository<Trade> {
    Optional<Trade> findByName(TradeDto tradeDto);
}
