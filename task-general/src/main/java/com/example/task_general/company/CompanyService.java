package com.example.task_general.company;

import com.example.task_general.exceptions.EntityNotPresentException;
import com.example.task_general.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;


    public Company findById(Long id) {
        return companyRepository.findById(id).orElseThrow(() -> new EntityNotPresentException("Company " + id + " non presente in db"));
    }

    public List<User> getUsersByCompanyId(Long id) {
        var company = findById(id);
        return company.getUsers().stream().filter(u -> u.getIsActive() && u.getIsConfirmed()).toList();
    }
}

