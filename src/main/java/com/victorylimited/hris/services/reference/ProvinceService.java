package com.victorylimited.hris.services.reference;

import com.victorylimited.hris.dtos.reference.ProvinceDTO;
import com.victorylimited.hris.dtos.reference.RegionDTO;

import java.util.List;

public interface ProvinceService {
    ProvinceDTO getById(Long id);
    List<ProvinceDTO> getAll(int page, int pageSize);
    List<ProvinceDTO> getProvinceByRegion(RegionDTO regionDTO);
}
