package com.merchstore.controllers;

import com.merchstore.dtos.econt.EcontOfficeDto;
import com.merchstore.services.EcontService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/econt")
public class EcontRestController {

    private final EcontService econtService;

    public EcontRestController(EcontService econtService) {
        this.econtService = econtService;
    }

    @GetMapping("/offices")
    public List<EcontOfficeDto> getOffices(
            @RequestParam(required = false) String city
    ) {

        if (city == null || city.isBlank()) {
            return econtService.getOffices();
        }

        return econtService.getOfficesByCity(city);
    }

    @GetMapping("/offices/{officeCode}")
    public EcontOfficeDto getOfficeByCode(
            @PathVariable String officeCode
    ) {
        return econtService.getOfficeByCode(officeCode);
    }
}