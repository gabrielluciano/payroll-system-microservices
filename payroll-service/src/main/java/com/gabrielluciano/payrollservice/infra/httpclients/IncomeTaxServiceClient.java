package com.gabrielluciano.payrollservice.infra.httpclients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import com.gabrielluciano.payrollservice.domain.model.IncomeTaxRate;

@FeignClient(name = "${services.incomeTaxService.name}", url = "${services.incomeTaxService.url}", path = "/income")
public interface IncomeTaxServiceClient {

    @GetMapping("/tax-rate")
    ResponseEntity<List<IncomeTaxRate>> getTaxRates();
}

