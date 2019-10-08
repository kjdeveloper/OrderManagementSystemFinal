package com.app.validation.impl;

import com.app.dto.ShopDto;
import com.app.exceptions.ExceptionCode;
import com.app.exceptions.MyException;

public class ShopValidator {

    public void validateShop(ShopDto shopDto){
        if (shopDto == null){
            throw new MyException(ExceptionCode.SHOP, "SHOP CANNOT BE NULL OR EMPTY");
        }
        if (!isNameValid(shopDto)){
            throw new MyException(ExceptionCode.SHOP, "INVALID NAME");
        }

    }
    private boolean isNameValid(ShopDto shopDTO){
        String MODEL_NAME_REGEX = "[A-Z ]+";
        return shopDTO.getName().matches(MODEL_NAME_REGEX);
    }

}
