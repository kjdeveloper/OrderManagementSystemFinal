package com.app.validation.impl;

import com.app.dto.CountryDto;
import com.app.exceptions.MyException;
import com.app.validation.generic.AbstractValidator;

import java.util.Map;

import static com.mysql.jdbc.StringUtils.isNullOrEmpty;

public class CountryValidator extends AbstractValidator<CountryDto> {

    @Override
    public Map<String, String> validate(final CountryDto countryDTO) {
        if (countryDTO == null) {
            errors.put("countryDTO", "null");
        }
        if (!isNameValid(countryDTO)) {
            errors.put("countryDTO name", "Name is not valid => " + countryDTO.getName());
        }
        return errors;
    }

    public void validateCountry(final CountryDto countryDto){
        if (isNullOrEmpty(countryDto.getName())){
            throw new MyException("COUNTRY CANNOT BE NULL OR EMPTY");
        }
        if (!isNameValid(countryDto)){
            throw new MyException("INVALID COUNTRY");
        }
    }

    private boolean isNameValid(final CountryDto countryDTO) {
        String MODEL_NAME_REGEX = "[A-Z ]+";
        return countryDTO.getName().matches(MODEL_NAME_REGEX);
    }


}
