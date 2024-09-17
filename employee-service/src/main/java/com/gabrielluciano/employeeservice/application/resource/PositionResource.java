package com.gabrielluciano.employeeservice.application.resource;

import com.gabrielluciano.employeeservice.domain.dto.CreatePositionRequest;
import com.gabrielluciano.employeeservice.domain.dto.PositionResponse;
import com.gabrielluciano.employeeservice.domain.service.PositionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("positions")
@RequiredArgsConstructor
@Slf4j
public class PositionResource {

    private final PositionService service;

    @GetMapping
    public ResponseEntity<List<PositionResponse>> list() {
        return ResponseEntity.ok(service.list());
    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody @Valid CreatePositionRequest body) {
        log.info("Received save position request");
        PositionResponse position = service.save(body);
        log.info("Successfully created position with id '{}'", position.id());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
