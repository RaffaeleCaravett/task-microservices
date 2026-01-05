package com.example.task_company.dtos.entitiesDTOS;

import com.example.task_company.validators.MinCurrentYear;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MetodoPagamentoDTO {

    private static final Integer CURRENT_YEAR = LocalDate.now().getYear() % 100;
    @NotEmpty(message = "Numero carta necessario")
    @Pattern(regexp = "^\\d{4}( \\d{4}){3}$", message = "Numero di carta non valido")
    private String cardNumber;
    @NotEmpty(message = "Mese carta necessario")
    @Min(value = 1, message = "Valore del mese troppo basso")
    @Max(value = 12, message = "Valore del mese troppo alto")
    private Integer month;
    @NotEmpty(message = "Anno carta necessario")
    @MinCurrentYear
    @Max(value = 99, message = "Anno carta troppo grande")
    private Integer year;
    @NotEmpty(message = "Codice segreto carta necessario")
    @Min(value = 111, message = "Codice segreto carta invalido")
    @Max(value = 999, message = "Codice segreto carta invalido")
    private String secretCode;
    @NotEmpty(message = "Owner carta necessario")
    @Pattern(regexp = "^.+\\s.+$", message = "Deve contenere almeno due parole separate da uno spazio")
    private String owner;
}
