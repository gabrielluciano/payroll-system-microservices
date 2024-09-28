package com.gabrielluciano.workattendanceservice.application.resource;

import com.gabrielluciano.workattendanceservice.domain.dto.CreateWorkAttendanceRequest;
import com.gabrielluciano.workattendanceservice.domain.dto.WorkAttendanceResponse;
import com.gabrielluciano.workattendanceservice.domain.service.WorkAttendanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("work-attendances")
@RequiredArgsConstructor
@Slf4j
public class WorkAttendanceResource {

    private final WorkAttendanceService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> save(@RequestBody @Valid CreateWorkAttendanceRequest body) {
        log.info("Received save work attendance request");
        service.save(body);
        log.info("Successfully created work attendance for cpf '{}', month '{}' and year '{}'",
                body.employeeCpf(), body.month(), body.year());
        return ResponseEntity.created(createLocation(body)).build();
    }

    @GetMapping("{cpf}/{year}/{month}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<WorkAttendanceResponse> findByCpfYearAndMonth(@PathVariable String cpf,
                                                                        @PathVariable Integer year,
                                                                        @PathVariable Integer month) {
        return ResponseEntity.ok(service.findByCpfYearAndMonth(cpf, year, month));
    }

    private URI createLocation(CreateWorkAttendanceRequest body) {
        return ServletUriComponentsBuilder.fromCurrentRequest()
                .pathSegment("{cpf}", "{year}", "{month}")
                .buildAndExpand(body.employeeCpf(), body.year(), body.month())
                .toUri();
    }
}
