package com.gabrielluciano.insstaxservice.application.resource;

import com.gabrielluciano.insstaxservice.domain.dto.CreateTaxRateRequest;
import com.gabrielluciano.insstaxservice.domain.service.TaxRateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("inss/tax-rate")
@RequiredArgsConstructor
@Slf4j
public class TaxRateResource {

    private final TaxRateService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> save(@RequestBody @Valid CreateTaxRateRequest body) {
        log.info("Received save tax rate request");
        Long id = service.save(body);
        log.info("Successfully created tax rate with id '{}'", id);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}