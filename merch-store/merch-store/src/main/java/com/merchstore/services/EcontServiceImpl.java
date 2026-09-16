package com.merchstore.services;

import com.merchstore.dtos.econt.EcontOfficeDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EcontServiceImpl implements EcontService {

    private final EcontApiClient econtApiClient;

    public EcontServiceImpl(EcontApiClient econtApiClient) {
        this.econtApiClient = econtApiClient;
    }

    @Override
    public List<EcontOfficeDto> getOffices() {
        return econtApiClient.getOffices();
    }

    @Override
    public List<EcontOfficeDto> getOfficesByCity(String city) {

        if (city == null || city.isBlank()) {
            throw new IllegalArgumentException(
                    "City is required!"
            );
        }

        String searchedCity = city.trim();

        return econtApiClient.getOffices()
                .stream()
                .filter(office ->
                        office.getCity() != null
                                && office.getCity()
                                .equalsIgnoreCase(searchedCity)
                )
                .toList();
    }

    @Override
    public EcontOfficeDto getOfficeByCode(String officeCode) {

        if (officeCode == null || officeCode.isBlank()) {
            throw new IllegalArgumentException(
                    "Econt office code is required!"
            );
        }

        String searchedCode = officeCode.trim();

        return econtApiClient.getOffices()
                .stream()
                .filter(office ->
                        office.getCode() != null
                                && office.getCode()
                                .equalsIgnoreCase(searchedCode)
                )
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Econt office with code "
                                        + searchedCode
                                        + " was not found!"
                        )
                );
    }
}