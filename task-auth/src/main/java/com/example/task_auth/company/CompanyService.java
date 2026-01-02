package com.example.task_auth.company;

import com.example.task_auth.codiceAccesso.CodiceAccesso;
import com.example.task_auth.codiceAccesso.CodiceAccessoRepository;
import com.example.task_auth.exceptions.exception.EntityNotPresentException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final CodiceAccessoRepository codiceAccessoRepository;

    public Company findById(Long id) {
        return companyRepository.findById(id).orElseThrow(() -> new EntityNotPresentException("Company " + id + " non presente in db"));
    }

    public Company findByEmail(String email) {
        return companyRepository.findByEmail(email).orElseThrow(() -> new EntityNotPresentException("Company " + email + " non presente in db"));
    }

    public CodiceAccesso createAccessCode(Long id, Company company) {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 6) {
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        CodiceAccesso codiceAccesso = new CodiceAccesso();
        codiceAccesso.setCompany(company != null ? company : companyRepository.findById(id).get());
        codiceAccesso.setCreationTime(Instant.now());
        codiceAccesso.setIsUsed(false);
        codiceAccesso.setCode(salt.toString());
        return codiceAccessoRepository.save(codiceAccesso);
    }

    public Boolean deleteAccessCode(Long id) {
        Optional<CodiceAccesso> codiceAccesso = codiceAccessoRepository.findByCompany_Id(id);
        if (codiceAccesso.isPresent()) {
            try {
                codiceAccessoRepository.deleteById(codiceAccesso.get().getId());
                return true;
            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }
    }
}

