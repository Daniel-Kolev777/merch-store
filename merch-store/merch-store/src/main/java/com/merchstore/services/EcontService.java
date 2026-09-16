package com.merchstore.services;

import com.merchstore.dtos.econt.EcontOfficeDto;

import java.util.List;

public interface EcontService {

    List<EcontOfficeDto> getOffices();

    List<EcontOfficeDto> getOfficesByCity(String city);

    EcontOfficeDto getOfficeByCode(String officeCode);
}