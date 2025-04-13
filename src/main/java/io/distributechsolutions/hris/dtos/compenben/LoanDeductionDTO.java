package io.distributechsolutions.hris.dtos.compenben;

import io.distributechsolutions.hris.dtos.BaseDTO;
import io.distributechsolutions.hris.dtos.profile.EmployeeDTO;

import java.math.BigDecimal;
import java.time.LocalDate;

public class LoanDeductionDTO extends BaseDTO {
    private EmployeeDTO employeeDTO;
    private String loanType;
    private String loanDescription;
    private BigDecimal loanAmount;
    private LocalDate loanStartDate;
    private LocalDate loanEndDate;
    private BigDecimal monthlyDeduction;

    public EmployeeDTO getEmployeeDTO() {
        return employeeDTO;
    }

    public void setEmployeeDTO(EmployeeDTO employeeDTO) {
        this.employeeDTO = employeeDTO;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public String getLoanDescription() {
        return loanDescription;
    }

    public void setLoanDescription(String loanDescription) {
        this.loanDescription = loanDescription;
    }

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }

    public LocalDate getLoanStartDate() {
        return loanStartDate;
    }

    public void setLoanStartDate(LocalDate loanStartDate) {
        this.loanStartDate = loanStartDate;
    }

    public LocalDate getLoanEndDate() {
        return loanEndDate;
    }

    public void setLoanEndDate(LocalDate loanEndDate) {
        this.loanEndDate = loanEndDate;
    }

    public BigDecimal getMonthlyDeduction() {
        return monthlyDeduction;
    }

    public void setMonthlyDeduction(BigDecimal monthlyDeduction) {
        this.monthlyDeduction = monthlyDeduction;
    }
}
