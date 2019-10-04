package com.app.service.services;

import com.app.dto.TradeDto;
import com.app.exceptions.ExceptionCode;
import com.app.exceptions.MyException;
import com.app.model.Trade;
import com.app.repository.TradeRepository;
import com.app.repository.impl.TradeRepositoryImpl;
import com.app.service.mapper.Mappers;
import com.app.validation.impl.TradeValidator;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TradeService {

    private TradeRepository tradeRepository;

    public TradeDto addTrade(TradeDto tradeDTO) {
        TradeValidator tradeValidator = new TradeValidator();
        tradeValidator.validateTrade(tradeDTO);

        var trade = tradeRepository.findByName(tradeDTO.getName()).orElse(null);

        if (trade == null) {
            trade = Mappers.fromTradeDtoToTrade(tradeDTO);
        } else {
            throw new MyException(ExceptionCode.TRADE, "TRADE WITH GIVEN NAME EXIST");
        }
        tradeRepository.addOrUpdate(trade).orElseThrow(() -> new MyException(ExceptionCode.TRADE, "CAN NOT ADD TRADE IN TRADE SERVICE"));
        return Mappers.fromTradeToTradeDto(trade);
    }
}
