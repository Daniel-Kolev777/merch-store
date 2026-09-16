package com.merchstore.services;

import com.merchstore.dtos.econt.EcontDeliveryPriceRequestDto;
import com.merchstore.dtos.econt.EcontOfficeDto;

import java.math.BigDecimal;
import java.util.List;

public interface EcontApiClient {

    List<EcontOfficeDto> getOffices();

    BigDecimal calculateOfficeDeliveryPrice(
            EcontDeliveryPriceRequestDto requestDto
    );
}