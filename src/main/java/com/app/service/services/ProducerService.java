package com.app.service.services;

import com.app.dto.ProducerDto;
import com.app.dto.TradeDto;
import com.app.exceptions.ExceptionCode;
import com.app.exceptions.MyException;
import com.app.model.*;
import com.app.repository.CountryRepository;
import com.app.repository.ProducerRepository;
import com.app.repository.StockRepository;
import com.app.repository.TradeRepository;
import com.app.repository.impl.CountryRepositoryImpl;
import com.app.repository.impl.ProducerRepositoryImpl;
import com.app.repository.impl.StockRepositoryImpl;
import com.app.repository.impl.TradeRepositoryImpl;
import com.app.service.mapper.Mappers;
import com.app.validation.impl.CountryValidator;
import com.app.validation.impl.ProducerValidator;
import com.app.validation.impl.TradeValidator;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ProducerService {

    private final ProducerRepository producerRepository = new ProducerRepositoryImpl();
    private final ProducerValidator producerValidator = new ProducerValidator();
    private final TradeValidator tradeValidator = new TradeValidator();
    private final CountryValidator countryValidator = new CountryValidator();
    private final CountryRepository countryRepository = new CountryRepositoryImpl();
    private final TradeRepository tradeRepository = new TradeRepositoryImpl();
    private final StockRepository stockRepository = new StockRepositoryImpl();

    public ProducerDto addProducer(ProducerDto producerDTO) {
        producerValidator.validateProducer(producerDTO);

        final var exist = producerRepository.isExistByNameAndTradeAndCountry(producerDTO);

        if (exist) {
            throw new MyException(ExceptionCode.PRODUCER, "PRODUCER WITH GIVEN NAME, TRADE AND COUNTRY EXIST");
        }

        var tradeDto = producerDTO.getTradeDto();

        var producer = producerRepository.findByName(producerDTO.getName()).orElse(null);
        var country = countryRepository.findByName(producerDTO.getCountryDto().getName()).orElse(null);
        var trade = tradeRepository.findByName(tradeDto.getName()).orElse(null);

        if (country == null){
            countryValidator.validateCountry(producerDTO.getCountryDto());
            country = Mappers.fromCountryDtoToCountry(producerDTO.getCountryDto());
            country = countryRepository.addOrUpdate(country).orElseThrow(() -> new MyException(ExceptionCode.COUNTRY, "CAN NOT ADD COUNTRY"));
        }
        if (trade == null){
            tradeValidator.validateTrade(producerDTO.getTradeDto());
            trade = Mappers.fromTradeDtoToTrade(producerDTO.getTradeDto());
            trade = tradeRepository.addOrUpdate(trade).orElseThrow(() -> new MyException(ExceptionCode.TRADE, "CAN NOT ADD TRADE"));
        }
        if (producer == null){
            producer = Mappers.fromProducerDtoToProducer(producerDTO);
        }

        producer.setTrade(trade);
        producer.setCountry(country);
        producerRepository.addOrUpdate(producer);
        return Mappers.fromProducerToProducerDto(producer);
    }

    public Set<ProducerDto> findProducerWithGivenBrandAndTheBiggerQuantityProducedThanGiven(String tradeName, Long quantity) {
        return stockRepository.findProducerWithGivenBrandAndTheBiggerQuantityProducedThanGiven(tradeName, quantity)
                .stream()
                .map(Mappers::fromProducerToProducerDto)
                .collect(Collectors.toSet());
    }
}
