package io.softwaregarage.hris.compenben.services.impls;

import io.softwaregarage.hris.compenben.dtos.RatesDTO;
import io.softwaregarage.hris.profile.dtos.EmployeeProfileDTO;
import io.softwaregarage.hris.compenben.entities.Rates;
import io.softwaregarage.hris.compenben.repositories.RatesRepository;
import io.softwaregarage.hris.profile.repositories.EmployeeProfileRepository;
import io.softwaregarage.hris.compenben.services.RatesService;
import io.softwaregarage.hris.profile.services.impls.EmployeeProfileServiceImpl;
import io.softwaregarage.hris.profile.services.EmployeeProfileService;

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
public class RatesServiceImpl implements RatesService {
    private final Logger logger = LoggerFactory.getLogger(RatesServiceImpl.class);

    private final RatesRepository ratesRepository;
    private final EmployeeProfileRepository employeeProfileRepository;

    public RatesServiceImpl(RatesRepository ratesRepository, EmployeeProfileRepository employeeProfileRepository) {
        this.ratesRepository = ratesRepository;
        this.employeeProfileRepository = employeeProfileRepository;
    }

    @Override
    public void saveOrUpdate(RatesDTO object) {
        Rates rates;
        String logMessage;

        if (object.getId() != null) {
            rates = ratesRepository.getReferenceById(object.getId());
            logMessage = "Employee's rates record with id ".concat(object.getId().toString()).concat(" is successfully updated.");
        } else {
            rates = new Rates();
            rates.setCreatedBy(object.getCreatedBy());
            rates.setDateAndTimeCreated(LocalDateTime.now(ZoneId.of("Asia/Manila")));
            logMessage = "Employee's rates record is successfully created.";
        }

        rates.setEmployee(employeeProfileRepository.getReferenceById(object.getEmployeeDTO().getId()));
        rates.setRateType(object.getRateType());
        rates.setBasicCompensationRate(object.getBasicCompensationRate());
        rates.setDailyCompensationRate(object.getDailyCompensationRate());
        rates.setHourlyCompensationRate(object.getHourlyCompensationRate());
        rates.setOvertimeHourlyCompensationRate(object.getOvertimeHourlyCompensationRate());
        rates.setLateHourlyDeductionRate(object.getLateHourlyDeductionRate());
        rates.setDailyAbsentDeductionRate(object.getDailyAbsentDeductionRate());
        rates.setUpdatedBy(object.getUpdatedBy());
        rates.setDateAndTimeUpdated(LocalDateTime.now(ZoneId.of("Asia/Manila")));

        ratesRepository.save(rates);
        logger.info(logMessage);
    }

    @Override
    public RatesDTO getById(UUID id) {
        logger.info("Retrieving employee's rates record with UUID ".concat(id.toString()));

        Rates rates = ratesRepository.getReferenceById(id);
        RatesDTO ratesDTO = new RatesDTO();

        ratesDTO.setId(rates.getId());
        ratesDTO.setEmployeeDTO(new EmployeeProfileServiceImpl(employeeProfileRepository).getById(rates.getEmployee().getId()));
        ratesDTO.setRateType(rates.getRateType());
        ratesDTO.setBasicCompensationRate(rates.getBasicCompensationRate());
        ratesDTO.setDailyCompensationRate(rates.getDailyCompensationRate());
        ratesDTO.setHourlyCompensationRate(rates.getHourlyCompensationRate());
        ratesDTO.setOvertimeHourlyCompensationRate(rates.getOvertimeHourlyCompensationRate());
        ratesDTO.setLateHourlyDeductionRate(rates.getLateHourlyDeductionRate());
        ratesDTO.setDailyAbsentDeductionRate(rates.getDailyAbsentDeductionRate());
        ratesDTO.setUpdatedBy(rates.getUpdatedBy());
        ratesDTO.setCreatedBy(rates.getCreatedBy());
        ratesDTO.setDateAndTimeCreated(rates.getDateAndTimeCreated());
        ratesDTO.setUpdatedBy(rates.getUpdatedBy());
        ratesDTO.setDateAndTimeUpdated(rates.getDateAndTimeUpdated());

        logger.info("Employee's rates record with id ".concat(id.toString()).concat(" is successfully retrieved."));

        return ratesDTO;
    }

    @Override
    public void delete(RatesDTO object) {
        if (object != null) {
            logger.warn("You are about to delete the employee's rates record permanently.");

            String id = object.getId().toString();
            Rates rates = ratesRepository.getReferenceById(object.getId());
            ratesRepository.delete(rates);

            logger.info("Employee's rates record with id ".concat(id).concat(" is successfully deleted."));
        }
    }

    @Override
    public List<RatesDTO> getAll(int page, int pageSize) {
        logger.info("Retrieving employee's rates records from the database.");
        List<Rates> ratesList = ratesRepository.findAll(PageRequest.of(page, pageSize)).stream().toList();

        logger.info("Employee's rates records successfully retrieved.");
        List<RatesDTO> ratesDTOList = new ArrayList<>();

        if (!ratesList.isEmpty()) {
            EmployeeProfileService employeeProfileService = new EmployeeProfileServiceImpl(employeeProfileRepository);

            for (Rates rates : ratesList) {
                RatesDTO ratesDTO = new RatesDTO();

                ratesDTO.setId(rates.getId());
                ratesDTO.setEmployeeDTO(employeeProfileService.getById(rates.getEmployee().getId()));
                ratesDTO.setRateType(rates.getRateType());
                ratesDTO.setBasicCompensationRate(rates.getBasicCompensationRate());
                ratesDTO.setDailyCompensationRate(rates.getDailyCompensationRate());
                ratesDTO.setHourlyCompensationRate(rates.getHourlyCompensationRate());
                ratesDTO.setOvertimeHourlyCompensationRate(rates.getOvertimeHourlyCompensationRate());
                ratesDTO.setLateHourlyDeductionRate(rates.getLateHourlyDeductionRate());
                ratesDTO.setDailyAbsentDeductionRate(rates.getDailyAbsentDeductionRate());
                ratesDTO.setUpdatedBy(rates.getUpdatedBy());
                ratesDTO.setCreatedBy(rates.getCreatedBy());
                ratesDTO.setDateAndTimeCreated(rates.getDateAndTimeCreated());
                ratesDTO.setUpdatedBy(rates.getUpdatedBy());
                ratesDTO.setDateAndTimeUpdated(rates.getDateAndTimeUpdated());

                ratesDTOList.add(ratesDTO);
            }

            logger.info(String.valueOf(ratesList.size()).concat(" record(s) found."));
        }

        return ratesDTOList;
    }

    @Override
    public List<RatesDTO> findByParameter(String param) {
        logger.info("Retrieving employee's rates records with search parameter '%".concat(param).concat("%' from the database."));

        List<RatesDTO> ratesDTOList = new ArrayList<>();
        List<Rates> ratesList = ratesList = ratesRepository.findByStringParameter(param);

        if (!ratesList.isEmpty()) {
            logger.info("Employee's rates records with parameter '%".concat(param).concat("%' has successfully retrieved."));

            EmployeeProfileService employeeProfileService = new EmployeeProfileServiceImpl(employeeProfileRepository);

            for (Rates rates : ratesList) {
                RatesDTO ratesDTO = new RatesDTO();

                ratesDTO.setId(rates.getId());
                ratesDTO.setEmployeeDTO(employeeProfileService.getById(rates.getEmployee().getId()));
                ratesDTO.setRateType(rates.getRateType());
                ratesDTO.setBasicCompensationRate(rates.getBasicCompensationRate());
                ratesDTO.setDailyCompensationRate(rates.getDailyCompensationRate());
                ratesDTO.setHourlyCompensationRate(rates.getHourlyCompensationRate());
                ratesDTO.setOvertimeHourlyCompensationRate(rates.getOvertimeHourlyCompensationRate());
                ratesDTO.setLateHourlyDeductionRate(rates.getLateHourlyDeductionRate());
                ratesDTO.setDailyAbsentDeductionRate(rates.getDailyAbsentDeductionRate());
                ratesDTO.setUpdatedBy(rates.getUpdatedBy());
                ratesDTO.setCreatedBy(rates.getCreatedBy());
                ratesDTO.setDateAndTimeCreated(rates.getDateAndTimeCreated());
                ratesDTO.setUpdatedBy(rates.getUpdatedBy());
                ratesDTO.setDateAndTimeUpdated(rates.getDateAndTimeUpdated());

                ratesDTOList.add(ratesDTO);
            }

            logger.info(String.valueOf(ratesList.size()).concat(" record(s) found."));
        }

        return ratesDTOList;
    }

    @Override
    public RatesDTO findByEmployeeDTO(EmployeeProfileDTO employeeProfileDTO) {
        logger.info("Retrieving employee's rates record.");

        Rates rates = ratesRepository.findByEmployee(employeeProfileRepository.getReferenceById(employeeProfileDTO.getId()));
        RatesDTO ratesDTO = new RatesDTO();

        ratesDTO.setId(rates.getId());
        ratesDTO.setEmployeeDTO(employeeProfileDTO);
        ratesDTO.setRateType(rates.getRateType());
        ratesDTO.setBasicCompensationRate(rates.getBasicCompensationRate());
        ratesDTO.setDailyCompensationRate(rates.getDailyCompensationRate());
        ratesDTO.setHourlyCompensationRate(rates.getHourlyCompensationRate());
        ratesDTO.setOvertimeHourlyCompensationRate(rates.getOvertimeHourlyCompensationRate());
        ratesDTO.setLateHourlyDeductionRate(rates.getLateHourlyDeductionRate());
        ratesDTO.setDailyAbsentDeductionRate(rates.getDailyAbsentDeductionRate());
        ratesDTO.setUpdatedBy(rates.getUpdatedBy());
        ratesDTO.setCreatedBy(rates.getCreatedBy());
        ratesDTO.setDateAndTimeCreated(rates.getDateAndTimeCreated());
        ratesDTO.setUpdatedBy(rates.getUpdatedBy());
        ratesDTO.setDateAndTimeUpdated(rates.getDateAndTimeUpdated());

        logger.info("Employee's rates record with id has successfully retrieved.");

        return ratesDTO;
    }
}
