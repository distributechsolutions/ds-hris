package io.distributechsolutions.hris.services.impls.reference;

import io.distributechsolutions.hris.dtos.reference.ProvinceDTO;
import io.distributechsolutions.hris.dtos.reference.RegionDTO;
import io.distributechsolutions.hris.entities.reference.Province;
import io.distributechsolutions.hris.repositories.reference.ProvinceRepository;
import io.distributechsolutions.hris.services.reference.ProvinceService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProvinceServiceImpl implements ProvinceService {
    private final ProvinceRepository provinceRepository;

    public ProvinceServiceImpl(ProvinceRepository provinceRepository) {
        this.provinceRepository = provinceRepository;
    }

    @Override
    public ProvinceDTO getById(Long id) {
        ProvinceDTO provinceDTO = null;

        if (id != null) {
            Province province = provinceRepository.getReferenceById(id);
            provinceDTO = new ProvinceDTO();
            provinceDTO.setId(province.getId());
            provinceDTO.setPsgCode(province.getPsgCode());
            provinceDTO.setProvinceDescription(province.getProvinceDescription());
            provinceDTO.setRegionCode(province.getRegionCode());
            provinceDTO.setProvinceCode(province.getProvinceCode());
        }

        return provinceDTO;
    }

    @Override
    public List<ProvinceDTO> getAll(int page, int pageSize) {
        List<ProvinceDTO> provinceDTOList = new ArrayList<>();
        List<Province> provinceList = provinceRepository.findAll(PageRequest.of(page, pageSize)).stream().toList();

        if (!provinceList.isEmpty()) {
            for (Province province : provinceList) {
                ProvinceDTO provinceDTO = new ProvinceDTO();
                provinceDTO.setId(province.getId());
                provinceDTO.setPsgCode(province.getPsgCode());
                provinceDTO.setProvinceDescription(province.getProvinceDescription());
                provinceDTO.setRegionCode(province.getRegionCode());
                provinceDTO.setProvinceCode(province.getProvinceCode());

                provinceDTOList.add(provinceDTO);
            }
        }

        return provinceDTOList;
    }

    @Override
    public List<ProvinceDTO> getProvinceByRegion(RegionDTO regionDTO) {
        List<Province> provinceList;
        List<ProvinceDTO> provinceDTOList = new ArrayList<>();

        if (regionDTO != null) {
            provinceList = provinceRepository.findProvincesByRegionCode(regionDTO.getRegionCode());

            if (!provinceList.isEmpty()) {
                for (Province province : provinceList) {
                    ProvinceDTO provinceDTO = new ProvinceDTO();
                    provinceDTO.setId(province.getId());
                    provinceDTO.setPsgCode(province.getPsgCode());
                    provinceDTO.setProvinceDescription(province.getProvinceDescription());
                    provinceDTO.setRegionCode(province.getRegionCode());
                    provinceDTO.setProvinceCode(province.getProvinceCode());

                    provinceDTOList.add(provinceDTO);
                }
            }
        }

        return provinceDTOList;
    }
}
