package com.merchstore.services;

import com.merchstore.config.EcontConfig;
import com.merchstore.dtos.econt.EcontApiAddressDto;
import com.merchstore.dtos.econt.EcontApiCityDto;
import com.merchstore.dtos.econt.EcontApiOfficeDto;
import com.merchstore.dtos.econt.EcontDeliveryPriceRequestDto;
import com.merchstore.dtos.econt.EcontOfficeDto;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class EcontApiClientImpl implements EcontApiClient {

    private final RestClient restClient;
    private final EcontConfig econtConfig;

    public EcontApiClientImpl(EcontConfig econtConfig) {

        this.econtConfig = econtConfig;

        this.restClient = RestClient.builder()
                .baseUrl(econtConfig.getBaseUrl())
                .defaultHeaders(headers ->
                        headers.setBasicAuth(
                                econtConfig.getUsername(),
                                econtConfig.getPassword()
                        )
                )
                .build();
    }

    @Override
    public List<EcontOfficeDto> getOffices() {

        try {

            EcontOfficesResponse response = restClient
                    .post()
                    .uri("/Nomenclatures/NomenclaturesService.getOffices.json")
                    .header(
                            HttpHeaders.CONTENT_TYPE,
                            "application/json"
                    )
                    .body("""
                            {
                                "countryCode": "BGR"
                            }
                            """)
                    .retrieve()
                    .body(EcontOfficesResponse.class);

            if (response == null || response.getOffices() == null) {
                return Collections.emptyList();
            }

            return response.getOffices()
                    .stream()
                    .map(this::mapOffice)
                    .toList();

        } catch (Exception e) {

            throw new BadCredentialsException(
                    "Failed to retrieve Econt offices!"
            );
        }
    }

    @Override
    public BigDecimal calculateOfficeDeliveryPrice(
            EcontDeliveryPriceRequestDto requestDto
    )  {

        try {

            Map<String, Object> requestBody = Map.of(
                    "label", Map.of(
                            "senderClient", Map.of(
                                    "name", econtConfig.getSenderName(),
                                    "phones", List.of(
                                            econtConfig.getSenderPhone()
                                    )
                            ),
                            "senderOfficeCode",
                            econtConfig.getSenderOfficeCode(),

                            "receiverClient", Map.of(
                                    "name", requestDto.getCustomerName(),
                                    "phones", List.of(
                                            requestDto.getCustomerPhone()
                                    )
                            ),
                            "receiverOfficeCode",
                            requestDto.getOfficeCode(),

                            "packCount", 1,
                            "shipmentType", "PACK",
                            "weight", requestDto.getWeight(),

                            "shipmentDescription",
                            "Merch Store order"
                    ),

                    "mode", "calculate"
            );

            EcontDeliveryPriceResponse response =
                    restClient
                            .post()
                            .uri(
                                    "/Shipments/LabelService.createLabel.json"
                            )
                            .header(
                                    HttpHeaders.CONTENT_TYPE,
                                    "application/json"
                            )
                            .body(requestBody)
                            .retrieve()
                            .body(
                                    EcontDeliveryPriceResponse.class
                            );

            if (response == null
                    || response.getLabel() == null
                    || response.getLabel().getTotalPrice() == null) {

                throw new IllegalStateException(
                        "Econt did not return delivery price!"
                );
            }

            return response.getLabel().getTotalPrice();

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to calculate Econt delivery price!",
                    e
            );
        }
    }

    private EcontOfficeDto mapOffice(
            EcontApiOfficeDto office
    ) {

        EcontApiAddressDto address =
                office.getAddress();

        String city = null;
        String fullAddress = null;

        if (address != null) {

            fullAddress =
                    address.getFullAddress();

            EcontApiCityDto apiCity =
                    address.getCity();

            if (apiCity != null) {
                city = apiCity.getName();
            }
        }

        return new EcontOfficeDto(
                office.getId(),
                office.getCode(),
                office.getName(),
                city,
                fullAddress
        );
    }

    private static class EcontOfficesResponse {

        private List<EcontApiOfficeDto> offices;

        public EcontOfficesResponse() {
        }

        public List<EcontApiOfficeDto> getOffices() {
            return offices;
        }

        public void setOffices(
                List<EcontApiOfficeDto> offices
        ) {
            this.offices = offices;
        }
    }

    private static class EcontDeliveryPriceResponse {

        private EcontDeliveryPriceLabel label;

        public EcontDeliveryPriceResponse() {
        }

        public EcontDeliveryPriceLabel getLabel() {
            return label;
        }

        public void setLabel(
                EcontDeliveryPriceLabel label
        ) {
            this.label = label;
        }
    }

    private static class EcontDeliveryPriceLabel {

        private BigDecimal totalPrice;
        private String currency;

        public EcontDeliveryPriceLabel() {
        }

        public BigDecimal getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(
                BigDecimal totalPrice
        ) {
            this.totalPrice = totalPrice;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(
                String currency
        ) {
            this.currency = currency;
        }
    }
}