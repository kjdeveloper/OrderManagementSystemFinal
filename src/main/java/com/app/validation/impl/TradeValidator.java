package com.app.validation.impl;

import com.app.dto.TradeDto;
import com.app.exceptions.ExceptionCode;
import com.app.exceptions.MyException;

public class TradeValidator{

    public void validateTrade(TradeDto tradeDto){
        if (tradeDto == null){
            throw new MyException(ExceptionCode.TRADE, "TRADE IS NULL");
        }
        if (!isNameValid(tradeDto)){
            throw new MyException(ExceptionCode.TRADE, "TRADE NAME IS NOT VALID");
        }
    }

    private boolean isNameValid(TradeDto tradeDto) {
        String MODEL_NAME_REGEX = "[A-Z ]+";
        return tradeDto.getName().matches(MODEL_NAME_REGEX);
    }
}
