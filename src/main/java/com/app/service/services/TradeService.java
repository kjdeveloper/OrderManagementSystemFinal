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

   /* private boolean validate(TradeDto tradeDTO) {

        Map<String, String> tradeErrorsMap = tradeValidator.validate(tradeDTO);
        if (tradeValidator.hasErrors()) {
            System.out.println("------TRADE VALIDATION ERRORS------");
            tradeErrorsMap.forEach((k, v) -> System.out.println(k + " -> " + v));
            System.out.println("-----------------------------------");
        }

        return !tradeValidator.hasErrors();
    }*/

    public void addTrade(TradeDto tradeDTO) {
        tradeValidator.validateTrade(tradeDTO);

        Trade trade = tradeRepository.findByName(tradeDTO).orElse(null);

        if (trade == null) {
            trade = Mappers.fromTradeDTOToTrade(tradeDTO);
        } else {
            throw new MyException("TRADE WITH GIVEN NAME EXIST");
        }

        tradeRepository.addOrUpdate(trade);

    }


}
