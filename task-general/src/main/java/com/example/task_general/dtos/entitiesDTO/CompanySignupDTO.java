package com.example.task_general.dtos.entitiesDTO;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CompanySignupDTO {
    @NotNull(message = "Campo necessario: Ragione sociale")
    private String ragioneSociale;
    @NotNull(message = "Campo necessario: Partita Iva")
    @Pattern(regexp = "^[0-9]{11}$")
    private String partitaIva;
    @NotNull(message = "Campo necessario: Forma Giuridica")
    private Long formaGiuridica;
    @NotNull(message = "Campo necessario: Paese di Registrazione")
    private Long paeseDiRegistrazione;
    @NotNull(message = "Campo necessario: Città")
    private Long citta;
    @NotNull(message = "Campo necessario: Cap")
    private Long cap;
    @NotNull(message = "Campo necessario: Regione")
    private Long regione;
    @NotNull(message = "Campo necessario: Via")
    private String via;
    @NotNull(message = "Campo necessario: Settore")
    private Long settore; @NotNull(message = "Campo necessario: Nome azienda")
    private String nomeAzienda;
    @NotNull(message = "Campo necessario: Email")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}$", message = "Email non valida")
    private String email;
    @NotNull(message = "Campo necessario: Password")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password non sicura")
    private String password;
    @Nullable
    @Max(9999999)
    @Min(1)
    private String dimensioniAzienda;
    @Nullable
    private Long paeseDiRegistrazioneSede;
    @Nullable
    private Long cittaSede;
    @Nullable
    private Long capSede;
    @Nullable
    private Long regioneSede;
    @Nullable
    private String viaSede;
}
