package com.gabrielluciano.payrollservice.infra.httpclients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import com.gabrielluciano.payrollservice.domain.model.InssTaxRate;

@FeignClient(name = "${services.inssTaxService.name}", url = "${services.inssTaxService.url}", path = "/inss")
public interface InssTaxServiceClient {

    @GetMapping("/tax-rate")
    ResponseEntity<List<InssTaxRate>> getTaxRates();
}

