package com.app.repository;

import com.app.dto.CountryDto;
import com.app.model.Country;
import com.app.repository.generic.GenericRepository;

import java.util.Optional;

public interface CountryRepository extends GenericRepository<Country> {
    Optional<Country> findByName(String countryName);
}
