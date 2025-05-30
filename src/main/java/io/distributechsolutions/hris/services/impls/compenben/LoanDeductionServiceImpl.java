package io.distributechsolutions.hris.services.impls.compenben;

import io.distributechsolutions.hris.dtos.compenben.LoanDeductionDTO;
import io.distributechsolutions.hris.entities.compenben.LoanDeduction;
import io.distributechsolutions.hris.repositories.compenben.LoanDeductionRepository;
import io.distributechsolutions.hris.repositories.profile.EmployeeRepository;
import io.distributechsolutions.hris.services.compenben.LoanDeductionService;
import io.distributechsolutions.hris.services.impls.profile.EmployeeServiceImpl;
import io.distributechsolutions.hris.services.profile.EmployeeService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class LoanDeductionServiceImpl implements LoanDeductionService {
    private final Logger logger = LoggerFactory.getLogger(LoanDeductionServiceImpl.class);

    private final LoanDeductionRepository loanDeductionRepository;
    private final EmployeeRepository employeeRepository;

    public LoanDeductionServiceImpl(LoanDeductionRepository loanDeductionRepository,
                                    EmployeeRepository employeeRepository) {
        this.loanDeductionRepository = loanDeductionRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public void saveOrUpdate(LoanDeductionDTO object) {
        LoanDeduction loanDeduction;
        String logMessage;

        if (object.getId() != null) {
            loanDeduction = loanDeductionRepository.getReferenceById(object.getId());
            logMessage = "Employee's loan deduction record with id ".concat(object.getId().toString()).concat(" is successfully updated.");
        } else {
            loanDeduction = new LoanDeduction();
            loanDeduction.setCreatedBy(object.getCreatedBy());
            loanDeduction.setDateAndTimeCreated(LocalDateTime.now(ZoneId.of("Asia/Manila")));
            logMessage = "Employee's loan deduction record is successfully created.";
        }

        loanDeduction.setLoanType(object.getLoanType());
        loanDeduction.setLoanAmount(object.getLoanAmount());
        loanDeduction.setLoanDescription(object.getLoanDescription());
        loanDeduction.setLoanStartDate(object.getLoanStartDate());
        loanDeduction.setLoanEndDate(object.getLoanEndDate());
        loanDeduction.setMonthlyDeduction(object.getMonthlyDeduction());
        loanDeduction.setEmployee(employeeRepository.getReferenceById(object.getEmployeeDTO().getId()));
        loanDeduction.setUpdatedBy(object.getUpdatedBy());
        loanDeduction.setDateAndTimeUpdated(LocalDateTime.now(ZoneId.of("Asia/Manila")));

        loanDeductionRepository.save(loanDeduction);
        logger.info(logMessage);
    }

    @Override
    public LoanDeductionDTO getById(UUID id) {
        logger.info("Retrieving employee's loan deduction record with UUID ".concat(id.toString()));

        LoanDeduction loanDeduction = loanDeductionRepository.getReferenceById(id);
        LoanDeductionDTO loanDeductionDTO = new LoanDeductionDTO();

        loanDeductionDTO.setId(loanDeduction.getId());
        loanDeductionDTO.setLoanType(loanDeduction.getLoanType());
        loanDeductionDTO.setLoanDescription(loanDeduction.getLoanDescription());
        loanDeductionDTO.setLoanAmount(loanDeduction.getLoanAmount());
        loanDeductionDTO.setLoanStartDate(loanDeduction.getLoanStartDate());
        loanDeductionDTO.setLoanEndDate(loanDeduction.getLoanEndDate());
        loanDeductionDTO.setMonthlyDeduction(loanDeduction.getMonthlyDeduction());
        loanDeductionDTO.setEmployeeDTO(new EmployeeServiceImpl(employeeRepository).getById(loanDeduction.getEmployee().getId()));
        loanDeductionDTO.setCreatedBy(loanDeduction.getCreatedBy());
        loanDeductionDTO.setDateAndTimeCreated(loanDeduction.getDateAndTimeCreated());
        loanDeductionDTO.setUpdatedBy(loanDeduction.getUpdatedBy());
        loanDeductionDTO.setDateAndTimeUpdated(loanDeduction.getDateAndTimeUpdated());

        logger.info("Employee's loan deduction record with id ".concat(id.toString()).concat(" is successfully retrieved."));
        return loanDeductionDTO;
    }

    @Override
    public void delete(LoanDeductionDTO object) {
        if (object != null) {
            logger.warn("You are about to delete the employee's loan deduction record permanently.");

            String id = object.getId().toString();
            LoanDeduction loanDeduction = loanDeductionRepository.getReferenceById(object.getId());
            loanDeductionRepository.delete(loanDeduction);

            logger.info("Employee's loan deduction record with id ".concat(id).concat(" is successfully deleted."));
        }
    }

    @Override
    public List<LoanDeductionDTO> getAll(int page, int pageSize) {
        logger.info("Retrieving employee's loan deductions from the database.");
        List<LoanDeduction> loanDeductionList = loanDeductionRepository.findAll(PageRequest.of(page, pageSize)).stream().toList();

        logger.info("Employee's loan deductions successfully retrieved.");
        List<LoanDeductionDTO> loanDeductionDTOList = new ArrayList<>();

        if (!loanDeductionList.isEmpty()) {
            EmployeeService employeeService = new EmployeeServiceImpl(employeeRepository);

            for (LoanDeduction loanDeduction : loanDeductionList) {
                LoanDeductionDTO loanDeductionDTO = new LoanDeductionDTO();

                loanDeductionDTO.setId(loanDeduction.getId());
                loanDeductionDTO.setLoanType(loanDeduction.getLoanType());
                loanDeductionDTO.setLoanDescription(loanDeduction.getLoanDescription());
                loanDeductionDTO.setLoanAmount(loanDeduction.getLoanAmount());
                loanDeductionDTO.setLoanStartDate(loanDeduction.getLoanStartDate());
                loanDeductionDTO.setLoanEndDate(loanDeduction.getLoanEndDate());
                loanDeductionDTO.setMonthlyDeduction(loanDeduction.getMonthlyDeduction());
                loanDeductionDTO.setEmployeeDTO(employeeService.getById(loanDeduction.getEmployee().getId()));
                loanDeductionDTO.setCreatedBy(loanDeduction.getCreatedBy());
                loanDeductionDTO.setDateAndTimeCreated(loanDeduction.getDateAndTimeCreated());
                loanDeductionDTO.setUpdatedBy(loanDeduction.getUpdatedBy());
                loanDeductionDTO.setDateAndTimeUpdated(loanDeduction.getDateAndTimeUpdated());

                loanDeductionDTOList.add(loanDeductionDTO);
            }

            logger.info(String.valueOf(loanDeductionList.size()).concat(" record(s) found."));
        }

        return loanDeductionDTOList;
    }

    @Override
    public List<LoanDeductionDTO> findByParameter(String param) {
        logger.info("Retrieving employee's loan deductions with search parameter '%".concat(param).concat("%' from the database."));

        List<LoanDeduction> loanDeductionList = loanDeductionRepository.findByStringParameter(param);
        List<LoanDeductionDTO> loanDeductionDTOList = new ArrayList<>();

        if (!loanDeductionList.isEmpty()) {
            logger.info("Employee's loan deductions with parameter '%".concat(param).concat("%' has successfully retrieved."));

            EmployeeService employeeService = new EmployeeServiceImpl(employeeRepository);

            for (LoanDeduction loanDeduction : loanDeductionList) {
                LoanDeductionDTO loanDeductionDTO = new LoanDeductionDTO();

                loanDeductionDTO.setId(loanDeduction.getId());
                loanDeductionDTO.setLoanType(loanDeduction.getLoanType());
                loanDeductionDTO.setLoanDescription(loanDeduction.getLoanDescription());
                loanDeductionDTO.setLoanAmount(loanDeduction.getLoanAmount());
                loanDeductionDTO.setLoanStartDate(loanDeduction.getLoanStartDate());
                loanDeductionDTO.setLoanEndDate(loanDeduction.getLoanEndDate());
                loanDeductionDTO.setMonthlyDeduction(loanDeduction.getMonthlyDeduction());
                loanDeductionDTO.setEmployeeDTO(employeeService.getById(loanDeduction.getEmployee().getId()));
                loanDeductionDTO.setCreatedBy(loanDeduction.getCreatedBy());
                loanDeductionDTO.setDateAndTimeCreated(loanDeduction.getDateAndTimeCreated());
                loanDeductionDTO.setUpdatedBy(loanDeduction.getUpdatedBy());
                loanDeductionDTO.setDateAndTimeUpdated(loanDeduction.getDateAndTimeUpdated());

                loanDeductionDTOList.add(loanDeductionDTO);
            }

            logger.info(String.valueOf(loanDeductionList.size()).concat(" record(s) found."));
        }

        return loanDeductionDTOList;
    }
}
