package com.app.validation.impl;

import com.app.dto.StockDto;
import com.app.exceptions.MyException;

public class StockValidator{

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
