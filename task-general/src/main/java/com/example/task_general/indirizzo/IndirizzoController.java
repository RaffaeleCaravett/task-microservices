package com.example.task_general.indirizzo;

import com.example.task_general.formaGiuridica.FormaGiuridica;
import com.example.task_general.formaGiuridica.FormaGiuridicaRepository;
import com.example.task_general.indirizzo.cap.Cap;
import com.example.task_general.indirizzo.cap.CapRepository;
import com.example.task_general.indirizzo.citta.Citta;
import com.example.task_general.indirizzo.citta.CittaRepository;
import com.example.task_general.indirizzo.nazione.Nazione;
import com.example.task_general.indirizzo.nazione.NazioneRepository;
import com.example.task_general.indirizzo.regione.Regione;
import com.example.task_general.indirizzo.regione.RegioneRepository;
import com.example.task_general.settore.Settore;
import com.example.task_general.settore.SettoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/indirizzo")
@RequiredArgsConstructor
public class IndirizzoController {
}
