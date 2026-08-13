package com.merchstore.dtos;

public class CategoryCreateDto {

    private String name;

    public CategoryCreateDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
