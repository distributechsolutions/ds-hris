package com.victorylimited.hris.dtos.profile;

import com.victorylimited.hris.dtos.BaseDTO;
import com.victorylimited.hris.dtos.admin.PositionDTO;

public class EmployeePositionDTO extends BaseDTO {
    private EmployeeDTO employeeDTO;
    private PositionDTO positionDTO;
    private boolean currentPosition;

    public EmployeeDTO getEmployeeDTO() {
        return employeeDTO;
    }

    public void setEmployeeDTO(EmployeeDTO employeeDTO) {
        this.employeeDTO = employeeDTO;
    }

    public PositionDTO getPositionDTO() {
        return positionDTO;
    }

    public void setPositionDTO(PositionDTO positionDTO) {
        this.positionDTO = positionDTO;
    }

    public boolean isCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(boolean currentPosition) {
        this.currentPosition = currentPosition;
    }
}
