package com.example.task_general.company;

import com.example.task_general.exceptions.EntityNotPresentException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;


    public Company findById(Long id) {
        return companyRepository.findById(id).orElseThrow(() -> new EntityNotPresentException("Company " + id + " non presente in db"));
    }
}

