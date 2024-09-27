package com.gabrielluciano.incometaxservice.application.resource;

import com.gabrielluciano.incometaxservice.application.resource.exception.StandardError;
import com.gabrielluciano.incometaxservice.domain.dto.CreateTaxRateRequest;
import com.gabrielluciano.incometaxservice.domain.dto.TaxRateResponse;
import com.gabrielluciano.incometaxservice.domain.service.TaxRateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Tax Rate", description = "Tax Rate resource")
@RestController
@RequestMapping("income/tax-rate")
@RequiredArgsConstructor
@Slf4j
public class TaxRateResource {

    private final TaxRateService service;

    @Operation(summary = "Save tax rate", description = "Creates a new tax rate")
    @ApiResponse(responseCode = "201", description = "Successfully created")
    @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = StandardError.class)))
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> save(@RequestBody @Valid CreateTaxRateRequest body) {
        log.info("Received save tax rate request");
        Long id = service.save(body);
        log.info("Successfully created tax rate with id '{}'", id);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "List tax rates", description = "Returns a list of tax rates")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<TaxRateResponse>> list() {
        return ResponseEntity.ok(service.list());
    }


    @Operation(summary = "Delete tax rate", description = "Deletes the tax rate with the given id")
    @ApiResponse(responseCode = "204", description = "Successfully deleted")
    @ApiResponse(responseCode = "404", description = "Tax rate not found", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = StandardError.class)))
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        log.info("Received delete tax rate request");
        service.deleteById(id);
        log.info("Successfully deleted tax rate with id '{}'", id);
        return ResponseEntity.noContent().build();
    }
}
