package com.app.validation.impl;

import com.app.dto.StockDto;
import com.app.exceptions.MyException;
import com.app.validation.generic.AbstractValidator;

import java.util.Map;

public class StockValidator extends AbstractValidator<StockDto> {

    @Override
    public Map<String, String> validate(StockDto stockDTO) {
        if (stockDTO == null){
            errors.put("stockDTO", "null");
        }
        if (!isQuantityValid(stockDTO)){
            errors.put("stodkDTO quantity", "Quantity is not valid => " + stockDTO.getQuantity());
        }
        return errors;
    }

    public void validateStock(StockDto stockDto){
        if (stockDto == null){
            throw new MyException("STOCK IS NULL");
        }
        if (isQuantityValid(stockDto)){
            throw new MyException("QUANTITY CANNOT LESS THAN ZERO");
        }
    }

    private boolean isQuantityValid(StockDto stockDTO){
        return stockDTO.getQuantity() < 0;
    }

}
