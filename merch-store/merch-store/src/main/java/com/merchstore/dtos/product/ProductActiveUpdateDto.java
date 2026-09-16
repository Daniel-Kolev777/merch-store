package com.merchstore.dtos.product;

import jakarta.validation.constraints.NotNull;

public class ProductActiveUpdateDto {

    @NotNull(message = "Active value is required")
    private Boolean active;

    public ProductActiveUpdateDto() {
    }

    public ProductActiveUpdateDto(Boolean active) {
        this.active = active;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}