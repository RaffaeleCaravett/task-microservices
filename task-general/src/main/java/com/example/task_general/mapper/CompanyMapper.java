package com.example.task_general.mapper;

import com.example.task_general.company.Company;
import com.example.task_general.dtos.entitiesDTO.CompanyDTO;
import com.example.task_general.dtos.entitiesDTO.CompanySignupDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        config = MapperConfiguration.class)
public interface CompanyMapper {


    CompanyDTO toCompanyDTO(Company company);

    @Mapping(target = "indirizzo", ignore = true)
    @Mapping(target = "sedeLegale", ignore = true)
    @Mapping(target = "formaGiuridica", ignore = true)
    @Mapping(target = "settore", ignore = true)
    @Mapping(target = "dimensioniAzienda", ignore = true)
    Company toCompany(CompanySignupDTO csu);
}
