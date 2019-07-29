package com.app.validation.impl;

import com.app.dto.TradeDto;
import com.app.exceptions.MyException;
import com.app.validation.generic.AbstractValidator;

import java.util.Map;

import static com.mysql.jdbc.StringUtils.isNullOrEmpty;

public class TradeValidator extends AbstractValidator<TradeDto> {

    @Override
    public Map<String, String> validate(TradeDto tradeDTO) {
        return null;
    }

    public void validateTrade(TradeDto tradeDto){
        if (isNullOrEmpty(tradeDto.getName())){
            throw new MyException("TRADE CANNOT BE NULL OR EMPTY");
        }
        if (!isNameValid(tradeDto)){
            throw new MyException("INVALID TRADE NAME");
        }
    }

    private boolean isNameValid(TradeDto tradeDto) {
        String MODEL_NAME_REGEX = "[A-Z ]+";
        return tradeDto.getName().matches(MODEL_NAME_REGEX);
    }
}
