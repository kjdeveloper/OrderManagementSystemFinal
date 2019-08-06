package com.app.service.services;

import com.app.dto.ProducerDto;
import com.app.dto.TradeDto;
import com.app.exceptions.MyException;
import com.app.model.Country;
import com.app.model.Producer;
import com.app.model.Trade;
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

public class ProducerService {

    private ProducerRepository producerRepository = new ProducerRepositoryImpl();
    private ProducerValidator producerValidator = new ProducerValidator();
    private TradeValidator tradeValidator = new TradeValidator();
    private CountryValidator countryValidator = new CountryValidator();
    private CountryRepository countryRepository = new CountryRepositoryImpl();
    private TradeRepository tradeRepository = new TradeRepositoryImpl();

   /* private boolean validateProducer(ProducerDto producerDTO) {

        Map<String, String> producerErrorsMap = producerValidator.validate(producerDTO);
        if (producerValidator.hasErrors()) {
            System.out.println("------PRODUCER VALIDATION ERRORS------");
            producerErrorsMap.forEach((k, v) -> System.out.println(k + " -> " + v));
            System.out.println("--------------------------------------");
        }

        return !producerValidator.hasErrors();
    }*/

    public ProducerDto addProducer(ProducerDto producerDTO) {
        producerValidator.validateProducer(producerDTO);
        countryValidator.validateCountry(producerDTO.getCountryDTO());
        tradeValidator.validateTrade(producerDTO.getTradeDTO());

        final boolean exist = producerRepository.isExistByNameAndTradeAndCountry(producerDTO);

        if (exist) {
            throw new MyException("PRODUCER WITH GIVEN NAME, TRADE AND COUNTRY EXIST");
        }

        TradeDto tradeDto = producerDTO.getTradeDTO();

        Producer producer = producerRepository.findByName(producerDTO).orElse(null);
        Country country = countryRepository.findByName(producerDTO.getCountryDTO()).orElse(null);
        Trade trade = tradeRepository.findByName(tradeDto).orElse(null);

        if (country == null){
            country = Mappers.fromCountryDTOToCountry(producerDTO.getCountryDTO());
            country = countryRepository.addOrUpdate(country).orElseThrow(() -> new MyException("CANNOT ADD COUNTRY"));
        }
        if (trade == null){
            trade = Mappers.fromTradeDTOToTrade(producerDTO.getTradeDTO());
            trade = tradeRepository.addOrUpdate(trade).orElseThrow(() -> new MyException("CANNOT ADD TRADE"));
        }
        if (producer == null){
            producer = Mappers.fromProducerDTOToProducer(producerDTO);
        }

        producer.setTrade(trade);
        producer.setCountry(country);
        producerRepository.addOrUpdate(producer);
        return Mappers.fromProducerToProducerDTO(producer);
    }

}
