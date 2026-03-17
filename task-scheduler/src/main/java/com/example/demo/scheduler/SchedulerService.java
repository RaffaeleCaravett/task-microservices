package com.example.demo.scheduler;

import com.example.demo.codiceAccesso.CodiceAccesso;
import com.example.demo.codiceAccesso.CodiceAccessoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SchedulerService {
    @Value("${cron.time.execution.time}")
    private String cronTime;
    private final CodiceAccessoRepository codiceAccessoRepository;

    @Scheduled(cron = "${cron.time.execution.time}")
    @Transactional
    public void mantainAccessCodes() {
            var creationTime = Instant.now().minusSeconds(600);
                codiceAccessoRepository.deleteExpiredUnusedCodes(creationTime);
    }
}
