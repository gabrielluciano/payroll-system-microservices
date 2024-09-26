package com.gabrielluciano.incometaxservice.application.resource;

import com.gabrielluciano.incometaxservice.domain.dto.CreateTaxRateRequest;
import com.gabrielluciano.incometaxservice.domain.dto.TaxRateResponse;
import com.gabrielluciano.incometaxservice.domain.service.TaxRateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("income/tax-rate")
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

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<TaxRateResponse>> list() {
        return ResponseEntity.ok(service.list());
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        log.info("Received delete tax rate request");
        service.deleteById(id);
        log.info("Successfully deleted tax rate with id '{}'", id);
        return ResponseEntity.noContent().build();
    }
}
