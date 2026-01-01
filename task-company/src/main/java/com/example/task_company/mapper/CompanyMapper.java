package com.example.task_company.mapper;


import com.example.task_company.company.Company;
import com.example.task_company.dtos.entitiesDTOS.CompanyDTO;
import com.example.task_company.dtos.entitiesDTOS.CompanySignupDTO;
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
    Company toCompany(CompanySignupDTO csu);
}
