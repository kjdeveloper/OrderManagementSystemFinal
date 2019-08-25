package com.app.service.services;

import com.app.dto.TradeDto;
import com.app.exceptions.MyException;
import com.app.model.Trade;
import com.app.repository.TradeRepository;
import com.app.repository.impl.TradeRepositoryImpl;
import com.app.service.mapper.Mappers;
import com.app.validation.impl.TradeValidator;

public class TradeService {

    private TradeRepository tradeRepository = new TradeRepositoryImpl();
    private TradeValidator tradeValidator = new TradeValidator();

    public TradeDto addTrade(TradeDto tradeDTO) {
        tradeValidator.validateTrade(tradeDTO);

        Trade trade = tradeRepository.findByName(tradeDTO.getName()).orElse(null);

        if (trade == null) {
            trade = Mappers.fromTradeDtoToTrade(tradeDTO);
        } else {
            throw new MyException("TRADE WITH GIVEN NAME EXIST");
        }
        tradeRepository.addOrUpdate(trade).orElseThrow(() -> new MyException("CAN NOT ADD TRADE IN TRADE SERVICE"));
        return Mappers.fromTradeToTradeDto(trade);
    }
}
