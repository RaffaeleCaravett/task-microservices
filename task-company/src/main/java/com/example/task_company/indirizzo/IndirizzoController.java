package com.example.task_company.indirizzo;


import com.example.task_company.dimensioni.Dimensione;
import com.example.task_company.dimensioni.DimensioneRepository;
import com.example.task_company.formaGiuridica.FormaGiuridica;
import com.example.task_company.formaGiuridica.FormaGiuridicaRepository;
import com.example.task_company.indirizzo.cap.Cap;
import com.example.task_company.indirizzo.cap.CapRepository;
import com.example.task_company.indirizzo.citta.Citta;
import com.example.task_company.indirizzo.citta.CittaRepository;
import com.example.task_company.indirizzo.nazione.Nazione;
import com.example.task_company.indirizzo.nazione.NazioneRepository;
import com.example.task_company.indirizzo.regione.Regione;
import com.example.task_company.indirizzo.regione.RegioneRepository;
import com.example.task_company.settore.Settore;
import com.example.task_company.settore.SettoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/indirizzo")
@RequiredArgsConstructor
public class IndirizzoController {

    private final NazioneRepository nazioneRepository;
    private final RegioneRepository regioneRepository;
    private final CittaRepository cittaRepository;
    private final CapRepository capRepository;
    private final SettoreRepository settoreRepository;
    private final FormaGiuridicaRepository formaGiuridicaRepository;
    private final DimensioneRepository dimensioneRepository;

    @GetMapping("/settori")
    public List<Settore> findAllSettori() {
        return settoreRepository.findAll();
    }

    @GetMapping("/dimensioni")
    public List<Dimensione> findAllDimensioni() {
        return dimensioneRepository.findAll();
    }

    @GetMapping("/forme")
    public List<FormaGiuridica> findAllForme() {
        return formaGiuridicaRepository.findAll();
    }

    @GetMapping("/nazioni")
    public List<Nazione> findAllNations() {
        return nazioneRepository.findAll();
    }

    @GetMapping("/regioni/{id}")
    public List<Regione> findAllRegions(@PathVariable Long id) {
        return regioneRepository.findAllByNazione_Id(id);
    }

    @GetMapping("/citta/{id}")
    public List<Citta> findAllCities(@PathVariable Long id) {
        return cittaRepository.findAllByRegione_Id(id);
    }

    @GetMapping("/cap/{id}")
    public List<Cap> findAllCaps(@PathVariable Long id) {
        return capRepository.findAllByCitta_Id(id);
    }
}
