package com.example.task_company.auth;

import com.example.task_company.dtos.entitiesDTOS.CompanyDTO;
import com.example.task_company.dtos.entitiesDTOS.CompanySignupDTO;
import com.example.task_company.exceptions.SignupException;
import com.example.task_company.gateway.GeneralGateway;
import com.example.task_company.mailgun.MGSamples;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@RequiredArgsConstructor
@Log4j2
public class AuthService {
    private final GeneralGateway generalGateway;
    private final MGSamples mgSamples;

    public CompanyDTO signup(CompanySignupDTO companySignupDTO) {
        try {
            CompanyDTO companyDTO = generalGateway.signupCompany(companySignupDTO);
            mgSamples.sendSimpleMessage(companyDTO.getNome(), companyDTO.getEmail(), "Benvenuto da taskmastaer", "Benvuto! \n" +
                    "è un piacere averti a bordo. Eplora i nostri servizi e se dovessi avere bisogno \n" +
                    "non esitare a contattarci. \n" +
                    "Un caro saluto dal team di taskmanager e a presto!");
            return companyDTO;
        } catch (Exception e) {
            log.info(e.getMessage());
            throw new SignupException("C'è stato un errore nell'elaborazione della richiesta");
        }
    }
}
