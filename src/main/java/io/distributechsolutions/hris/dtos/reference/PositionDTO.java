package io.distributechsolutions.hris.dtos.reference;

import io.distributechsolutions.hris.dtos.BaseDTO;

public class PositionDTO extends BaseDTO {
    private String code;
    private String name;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
