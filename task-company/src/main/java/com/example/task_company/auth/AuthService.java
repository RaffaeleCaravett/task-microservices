package com.example.task_company.auth;

import com.example.task_company.codiceAccesso.CodiceAccesso;
import com.example.task_company.codiceAccesso.CodiceAccessoRepository;
import com.example.task_company.company.Company;
import com.example.task_company.company.CompanyRepository;
import com.example.task_company.company.Role;
import com.example.task_company.dimensioni.DimensioneRepository;
import com.example.task_company.dtos.entitiesDTOS.CompanyDTO;
import com.example.task_company.dtos.entitiesDTOS.CompanyLoginDTO;
import com.example.task_company.dtos.entitiesDTOS.CompanySignupDTO;
import com.example.task_company.dtos.entitiesDTOS.MetodoPagamentoDTO;
import com.example.task_company.exceptions.SignupException;
import com.example.task_company.exceptions.UnauthorizedException;
import com.example.task_company.exceptions.WrongDTOException;
import com.example.task_company.formaGiuridica.FormaGiuridica;
import com.example.task_company.formaGiuridica.FormaGiuridicaRepository;
import com.example.task_company.gateway.GeneralGateway;
import com.example.task_company.indirizzo.Indirizzo;
import com.example.task_company.indirizzo.IndirizzoRepository;
import com.example.task_company.indirizzo.cap.CapRepository;
import com.example.task_company.indirizzo.citta.CittaRepository;
import com.example.task_company.indirizzo.nazione.NazioneRepository;
import com.example.task_company.indirizzo.regione.RegioneRepository;
import com.example.task_company.mailgun.MGSamples;
import com.example.task_company.mapper.CompanyMapper;
import com.example.task_company.metodoPagamento.MetodoPagamento;
import com.example.task_company.metodoPagamento.MetodoPagamentoRepository;
import com.example.task_company.notification.Notification;
import com.example.task_company.notification.NotificationRepository;
import com.example.task_company.notification.NotificationState;
import com.example.task_company.piano.Piano;
import com.example.task_company.piano.PianoRepository;
import com.example.task_company.settore.Settore;
import com.example.task_company.settore.SettoreRepository;
import com.example.task_company.subscription.SubscriptionRepository;
import com.example.task_company.subscription.Subscription;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

@Service
@Validated
@RequiredArgsConstructor
@Log4j2
public class AuthService {
    private final GeneralGateway generalGateway;
    private final MGSamples mgSamples;
    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;
    private final CittaRepository cittaRepository;
    private final NazioneRepository nazioneRepository;
    private final RegioneRepository regioneRepository;
    private final CapRepository capRepository;
    private final IndirizzoRepository indirizzoRepository;
    private final SettoreRepository settoreRepository;
    private final FormaGiuridicaRepository formaGiuridicaRepository;
    private final PasswordEncoder passwordEncoder;
    private final CodiceAccessoRepository codiceAccessoRepository;
    private final PianoRepository pianoRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final NotificationRepository notificationRepository;
    private final DimensioneRepository dimensioneRepository;
    private final MetodoPagamentoRepository metodoPagamentoRepository;

    @Transactional
    public CompanyDTO signup(CompanySignupDTO companySignupDTO) {

        if (companyRepository.findByEmail(companySignupDTO.getEmail()).isPresent()) {
            throw new SignupException("L'email è già presente in db");
        }
        try {
            Company company = companyMapper.toCompany(companySignupDTO);
            if (null == companySignupDTO.getCap() || null == companySignupDTO.getCitta() || null == companySignupDTO.getRegione() || null == companySignupDTO.getPaeseDiRegistrazione()
                    || null == companySignupDTO.getVia()) {
                throw new WrongDTOException("Le informazioni sull'indirizzo sono incomplete");
            }
            List<Indirizzo> indirizzos = new ArrayList<>();
            indirizzos.add(generateIndirizzo(companySignupDTO, "indirizzo"));
            company.setIndirizzo(indirizzos);
            if (
                    (null != companySignupDTO.getCapSede() || null != companySignupDTO.getCittaSede() || null != companySignupDTO.getRegioneSede() || null != companySignupDTO.getPaeseDiRegistrazioneSede()
                            || null != companySignupDTO.getViaSede()) && (
                            null == companySignupDTO.getCapSede() || null == companySignupDTO.getCittaSede() || null == companySignupDTO.getRegioneSede() || null == companySignupDTO.getPaeseDiRegistrazioneSede()
                                    || null == companySignupDTO.getViaSede()
                    )
            ) {
                throw new WrongDTOException("Le informazioni sulla sede legale sono incomplete");
            } else {
                List<Indirizzo> sedi = new ArrayList<>();
                sedi.add(generateIndirizzo(companySignupDTO, "sede"));
                company.setSedeLegale(sedi);
            }
            FormaGiuridica formaGiuridica = formaGiuridicaRepository.findById(companySignupDTO.getFormaGiuridica()).orElseThrow(() ->
                    new SignupException("La forma giuridica non è corretta"));
            Settore settore = settoreRepository.findById(companySignupDTO.getFormaGiuridica()).orElseThrow(() ->
                    new SignupException("Il settore non è corretto"));
            company.setFormaGiuridica(formaGiuridica);
            company.setSettore(settore);
            company.setPassword(passwordEncoder.encode(companySignupDTO.getPassword()));
            company.setRole(Role.COMPANY);
            company.setCreatedAt(LocalDate.now().toString());
            company.setIsActive(true);
            company.setIsConfirmed(false);
            company.setDimensioniAzienda(dimensioneRepository.findById(companySignupDTO.getDimensioniAzienda()).orElseThrow(() -> new SignupException("La dimensione non è corretta")));
            companyRepository.save(company);
            CodiceAccesso codiceAccesso = createAccessCode(company.getId(), company);

            Subscription subscription = new Subscription();
            subscription.setCompany(company);
            subscription.setStartDate(LocalDate.now());
            if (companySignupDTO.getPianoId().equals(1L)) {
                subscription.setEndDate(LocalDate.now().plusDays(30));
            } else {
                subscription.setEndDate(LocalDate.now().plusDays(companySignupDTO.getSubscriptionDays()));
            }
            subscription.setIsActive(true);
            subscription.setPiano(pianoRepository.findById(companySignupDTO.getPianoId()).orElseThrow(() -> new Exception("Il piano che hai scelto non esiste")));
            subscriptionRepository.save(subscription);
            mgSamples.sendSimpleMessage(company.getNomeAzienda(), company.getEmail(), "Registrazione avvenuta - Welcome on TaskMaster",
                    "Ciao " + company.getNomeAzienda() + "!\n" +
                            "Il tuo codice di accesso è : \n" +
                            codiceAccesso.getCode() + " \n" +
                            "Ti diamo il benvenuto a bordo. Non farti sfuggire niente delle tue attività lavorative e/o quotidiane. \n" +
                            "Utilizza i nostri tutorial per prendere dimestichezza con gli strumenti e contattaci in caso di necessità!\n" +
                            "Di nuovo benvenuto a bordo,\n" +
                            "Con affetto,\n" +
                            "il tuo team di TaskMaster!");
            CompanyDTO companyDTO = new CompanyDTO();
            companyDTO.setEmail(companySignupDTO.getEmail());
            companyDTO.setId(company.getId());
            companyDTO.setNomeAzienda(companySignupDTO.getNomeAzienda());

            Notification notification = new Notification();
            notification.setText("Benvenuto! La registrazione è avvenuta con successo.");
            notification.setStato(NotificationState.SENT);
            notification.setCreatedAt(LocalDate.now());
            notification.setReceiver(company);
            notificationRepository.save(notification);

            MetodoPagamento metodoPagamento = new MetodoPagamento();
            metodoPagamento.setCompany(company);
            metodoPagamento.setIsActive(true);
            metodoPagamento.setAddedAt(LocalDate.now());
            metodoPagamento.setMonth(companySignupDTO.getMetodoPagamentoDTO().getMonth().toString());
            metodoPagamento.setYear(companySignupDTO.getMetodoPagamentoDTO().getYear().toString());
            metodoPagamento.setCardNumber(companySignupDTO.getMetodoPagamentoDTO().getCardNumber());
            metodoPagamento.setOwner(companySignupDTO.getMetodoPagamentoDTO().getOwner());
            metodoPagamento.setSecretCode(passwordEncoder.encode(companySignupDTO.getMetodoPagamentoDTO().getSecretCode()));
            metodoPagamentoRepository.save(metodoPagamento);
            return companyDTO;
        } catch (WrongDTOException e) {
            throw new WrongDTOException(e.getMessage());
        } catch (
                Exception e) {
            try {
                var company = companyRepository.findByEmail(companySignupDTO.getEmail());
                if (company.isPresent()) {
                    if (company.get().getCodiceAccesso() != null) {
                        codiceAccessoRepository.delete(company.get().getCodiceAccesso());
                    }
                    if (!company.get().getIndirizzo().isEmpty()) {
                        indirizzoRepository.deleteAll(company.get().getIndirizzo());
                    }
                    if (company.get().getSubscription() != null) {
                        subscriptionRepository.delete(company.get().getSubscription());
                    }
                    if (company.get().getNotifications() != null) {
                        notificationRepository.deleteAll(company.get().getNotifications());
                    }
                    if (company.get().getMetodoPagamento() != null) {
                        metodoPagamentoRepository.deleteAll(company.get().getMetodoPagamento());
                    }
                }
                throw new SignupException(e.getMessage());
            } catch (Exception ex) {
                throw new SignupException(e.getMessage());
            }
        }
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

    public Indirizzo generateIndirizzo(CompanySignupDTO companySignupDTO, String type) {
        if ("indirizzo".equals(type)) {
            Indirizzo indirizzo = new Indirizzo();
            indirizzo.setNazione(nazioneRepository.findById(companySignupDTO.getPaeseDiRegistrazione()).orElseThrow(() -> new SignupException("La nazione non è corretta")));
            indirizzo.setRegione(regioneRepository.findById(companySignupDTO.getRegione()).orElseThrow(() -> new SignupException("La regione non è corretta")));
            indirizzo.setCitta(cittaRepository.findById(companySignupDTO.getCitta()).orElseThrow(() -> new SignupException("La città non è corretta")));
            indirizzo.setCap(capRepository.findById(companySignupDTO.getCap()).orElseThrow(() -> new SignupException("Il cap non è corretto")));
            indirizzo.setVia(companySignupDTO.getVia());
            return indirizzoRepository.save(indirizzo);
        } else {
            Indirizzo indirizzo = new Indirizzo();
            indirizzo.setNazione(nazioneRepository.findById(companySignupDTO.getPaeseDiRegistrazioneSede()).orElseThrow(() -> new SignupException("La nazione non è corretta")));
            indirizzo.setRegione(regioneRepository.findById(companySignupDTO.getRegioneSede()).orElseThrow(() -> new SignupException("La regione non è corretta")));
            indirizzo.setCitta(cittaRepository.findById(companySignupDTO.getCittaSede()).orElseThrow(() -> new SignupException("La città non è corretta")));
            indirizzo.setCap(capRepository.findById(companySignupDTO.getCapSede()).orElseThrow(() -> new SignupException("Il cap non è corretto")));
            indirizzo.setVia(companySignupDTO.getViaSede());
            return indirizzoRepository.save(indirizzo);
        }
    }

    public Long findByEmail(CompanyLoginDTO companyLoginDTO) {
        Company user = companyRepository.findByEmail(companyLoginDTO.getEmail()).orElseThrow(() -> new UnauthorizedException("Credenziali errate"));
        if (passwordEncoder.matches(companyLoginDTO.getPassword(), user.getPassword())) {
            return user.getId();
        }
        throw new UnauthorizedException("Credenziali errate");
    }

    public List<Piano> getPiani() {
        return pianoRepository.findAll();
    }
}
