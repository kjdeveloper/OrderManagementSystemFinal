package com.app.service.services;

import com.app.dto.ProducerDto;
import com.app.dto.TradeDto;
import com.app.exceptions.MyException;
import com.app.model.*;
import com.app.repository.CountryRepository;
import com.app.repository.ProducerRepository;
import com.app.repository.TradeRepository;
import com.app.repository.impl.CountryRepositoryImpl;
import com.app.repository.impl.ProducerRepositoryImpl;
import com.app.repository.impl.TradeRepositoryImpl;
import com.app.service.mapper.Mappers;
import com.app.validation.impl.CountryValidator;
import com.app.validation.impl.ProducerValidator;
import com.app.validation.impl.TradeValidator;

import java.util.Set;
import java.util.stream.Collectors;

public class ProducerService {

    private ProducerRepository producerRepository = new ProducerRepositoryImpl();
    private ProducerValidator producerValidator = new ProducerValidator();
    private TradeValidator tradeValidator = new TradeValidator();
    private CountryValidator countryValidator = new CountryValidator();
    private CountryRepository countryRepository = new CountryRepositoryImpl();
    private TradeRepository tradeRepository = new TradeRepositoryImpl();

    public ProducerDto addProducer(ProducerDto producerDTO) {
        producerValidator.validateProducer(producerDTO);
        countryValidator.validateCountry(producerDTO.getCountryDto());
        tradeValidator.validateTrade(producerDTO.getTradeDto());

        final boolean exist = producerRepository.isExistByNameAndTradeAndCountry(producerDTO);

        if (exist) {
            throw new MyException("PRODUCER WITH GIVEN NAME, TRADE AND COUNTRY EXIST");
        }

        TradeDto tradeDto = producerDTO.getTradeDto();

        Producer producer = producerRepository.findByName(producerDTO.getName()).orElse(null);
        Country country = countryRepository.findByName(producerDTO.getCountryDto().getName()).orElse(null);
        Trade trade = tradeRepository.findByName(tradeDto.getName()).orElse(null);

        if (country == null){
            country = Mappers.fromCountryDtoToCountry(producerDTO.getCountryDto());
            country = countryRepository.addOrUpdate(country).orElseThrow(() -> new MyException("CANNOT ADD COUNTRY"));
        }
        if (trade == null){
            trade = Mappers.fromTradeDtoToTrade(producerDTO.getTradeDto());
            trade = tradeRepository.addOrUpdate(trade).orElseThrow(() -> new MyException("CANNOT ADD TRADE"));
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
        return producerRepository.findAll()
                .stream()
                .filter(producer -> producer.getTrade().getName().equals(tradeName))
                .map(Mappers::fromProducerToProducerDto)
                .collect(Collectors.toSet());
    }
}
