package com.gabrielluciano.workattendancepublishservice.application.resource;

import com.gabrielluciano.workattendancepublishservice.domain.dto.CreateWorkAttendanceRequest;
import com.gabrielluciano.workattendancepublishservice.domain.service.WorkAttendanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        log.info("Successfully published work attendance for cpf '{}', month '{}' and year '{}'",
                body.employeeCpf(), body.month(), body.year());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
