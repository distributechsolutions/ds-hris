package com.whizservices.hris.entities.payroll;

import com.whizservices.hris.entities.BaseEntity;
import com.whizservices.hris.entities.profile.Employee;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "sg_hris_employee_payroll")
public class EmployeePayroll extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = false)
    private Employee employee;

    @Column(name = "cut_off_from_date", nullable = false)
    private LocalDate cutOffFromDate;

    @Column(name = "cut_off_to_date", nullable = false)
    private LocalDate cutOffToDate;

    @Column(name = "payroll_frequency", length = 25, nullable = false)
    private String payrollFrequency;

    @Column(name = "basic_pay_amount", nullable = false)
    private BigDecimal basicPayAmount;

    @Column(name = "allowance_pay_amount", nullable = false)
    private BigDecimal allowancePayAmount;

    @Column(name = "absent_deduction_amount", nullable = false)
    private BigDecimal absentDeductionAmount;

    @Column(name = "late_or_undertime_deduction_amount", nullable = false)
    private BigDecimal lateOrUndertimeDeductionAmount;

    @Column(name = "rest_day_overtime_pay_amount", nullable = false)
    private BigDecimal restDayOvertimePayAmount;

    @Column(name = "night_differential_pay_amount", nullable = false)
    private BigDecimal nightDifferentialPayAmount;

    @Column(name = "leave_pay_amount", nullable = false)
    private BigDecimal leavePayAmount;

    @Column(name = "regular_holiday_pay_amount", nullable = false)
    private BigDecimal regularHolidayPayAmount;

    @Column(name = "special_holiday_pay_amount", nullable = false)
    private BigDecimal specialHolidayPayAmount;

    @Column(name = "adjustment_pay_amount", nullable = false)
    private BigDecimal adjustmentPayAmount;

    @Column(name = "total_gross_pay_amount", nullable = false)
    private BigDecimal totalGrossPayAmount;

    @Column(name = "sss_deduction_amount", nullable = false)
    private BigDecimal sssDeductionAmount;

    @Column(name = "hdmf_deduction_amount", nullable = false)
    private BigDecimal hdmfDeductionAmount;

    @Column(name = "philhealth_deduction_amount", nullable = false)
    private BigDecimal philhealthDeductionAmount;

    @Column(name = "withholding_tax_deduction_amount", nullable = false)
    private BigDecimal withholdingTaxDeductionAmount;

    @Column(name = "total_loan_deduction_amount", nullable = false)
    private BigDecimal totalLoanDeductionAmount;

    @Column(name = "other_deduction_amount", nullable = false)
    private BigDecimal otherDeductionAmount;

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public LocalDate getCutOffFromDate() {
        return cutOffFromDate;
    }

    public void setCutOffFromDate(LocalDate cutOffFromDate) {
        this.cutOffFromDate = cutOffFromDate;
    }

    public LocalDate getCutOffToDate() {
        return cutOffToDate;
    }

    public void setCutOffToDate(LocalDate cutOffToDate) {
        this.cutOffToDate = cutOffToDate;
    }

    public String getPayrollFrequency() {
        return payrollFrequency;
    }

    public void setPayrollFrequency(String payrollFrequency) {
        this.payrollFrequency = payrollFrequency;
    }

    public BigDecimal getBasicPayAmount() {
        return basicPayAmount;
    }

    public void setBasicPayAmount(BigDecimal basicPayAmount) {
        this.basicPayAmount = basicPayAmount;
    }

    public BigDecimal getAllowancePayAmount() {
        return allowancePayAmount;
    }

    public void setAllowancePayAmount(BigDecimal allowancePayAmount) {
        this.allowancePayAmount = allowancePayAmount;
    }

    public BigDecimal getAbsentDeductionAmount() {
        return absentDeductionAmount;
    }

    public void setAbsentDeductionAmount(BigDecimal absentDeductionAmount) {
        this.absentDeductionAmount = absentDeductionAmount;
    }

    public BigDecimal getLateOrUndertimeDeductionAmount() {
        return lateOrUndertimeDeductionAmount;
    }

    public void setLateOrUndertimeDeductionAmount(BigDecimal lateOrUndertimeDeductionAmount) {
        this.lateOrUndertimeDeductionAmount = lateOrUndertimeDeductionAmount;
    }

    public BigDecimal getRestDayOvertimePayAmount() {
        return restDayOvertimePayAmount;
    }

    public void setRestDayOvertimePayAmount(BigDecimal restDayOvertimePayAmount) {
        this.restDayOvertimePayAmount = restDayOvertimePayAmount;
    }

    public BigDecimal getNightDifferentialPayAmount() {
        return nightDifferentialPayAmount;
    }

    public void setNightDifferentialPayAmount(BigDecimal nightDifferentialPayAmount) {
        this.nightDifferentialPayAmount = nightDifferentialPayAmount;
    }

    public BigDecimal getLeavePayAmount() {
        return leavePayAmount;
    }

    public void setLeavePayAmount(BigDecimal leavePayAmount) {
        this.leavePayAmount = leavePayAmount;
    }

    public BigDecimal getRegularHolidayPayAmount() {
        return regularHolidayPayAmount;
    }

    public void setRegularHolidayPayAmount(BigDecimal regularHolidayPayAmount) {
        this.regularHolidayPayAmount = regularHolidayPayAmount;
    }

    public BigDecimal getSpecialHolidayPayAmount() {
        return specialHolidayPayAmount;
    }

    public void setSpecialHolidayPayAmount(BigDecimal specialHolidayPayAmount) {
        this.specialHolidayPayAmount = specialHolidayPayAmount;
    }

    public BigDecimal getAdjustmentPayAmount() {
        return adjustmentPayAmount;
    }

    public void setAdjustmentPayAmount(BigDecimal adjustmentPayAmount) {
        this.adjustmentPayAmount = adjustmentPayAmount;
    }

    public BigDecimal getTotalGrossPayAmount() {
        return totalGrossPayAmount;
    }

    public void setTotalGrossPayAmount(BigDecimal totalGrossPayAmount) {
        this.totalGrossPayAmount = totalGrossPayAmount;
    }

    public BigDecimal getSssDeductionAmount() {
        return sssDeductionAmount;
    }

    public void setSssDeductionAmount(BigDecimal sssDeductionAmount) {
        this.sssDeductionAmount = sssDeductionAmount;
    }

    public BigDecimal getHdmfDeductionAmount() {
        return hdmfDeductionAmount;
    }

    public void setHdmfDeductionAmount(BigDecimal hdmfDeductionAmount) {
        this.hdmfDeductionAmount = hdmfDeductionAmount;
    }

    public BigDecimal getPhilhealthDeductionAmount() {
        return philhealthDeductionAmount;
    }

    public void setPhilhealthDeductionAmount(BigDecimal philhealthDeductionAmount) {
        this.philhealthDeductionAmount = philhealthDeductionAmount;
    }

    public BigDecimal getWithholdingTaxDeductionAmount() {
        return withholdingTaxDeductionAmount;
    }

    public void setWithholdingTaxDeductionAmount(BigDecimal withholdingTaxDeductionAmount) {
        this.withholdingTaxDeductionAmount = withholdingTaxDeductionAmount;
    }

    public BigDecimal getTotalLoanDeductionAmount() {
        return totalLoanDeductionAmount;
    }

    public void setTotalLoanDeductionAmount(BigDecimal totalLoanDeductionAmount) {
        this.totalLoanDeductionAmount = totalLoanDeductionAmount;
    }

    public BigDecimal getOtherDeductionAmount() {
        return otherDeductionAmount;
    }

    public void setOtherDeductionAmount(BigDecimal otherDeductionAmount) {
        this.otherDeductionAmount = otherDeductionAmount;
    }
}
