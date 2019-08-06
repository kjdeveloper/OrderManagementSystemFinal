package com.app.validation.impl;

import com.app.dto.ShopDto;
import com.app.exceptions.MyException;

import static com.mysql.jdbc.StringUtils.isNullOrEmpty;

public class ShopValidator {

    public void validateShop(ShopDto shopDto){
        if (isNullOrEmpty(shopDto.getName())){
            throw new MyException("SHOP CANNOT BE NULL OR EMPTY");
        }
        if (!isNameValid(shopDto)){
            throw new MyException("INVALID SHOP NAME");
        }

    }
    private boolean isNameValid(ShopDto shopDTO){
        String MODEL_NAME_REGEX = "[A-Z ]+";
        return shopDTO.getName().matches(MODEL_NAME_REGEX);
    }

}
