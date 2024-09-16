package com.gabrielluciano.employeeservice.application.resource;

import com.gabrielluciano.employeeservice.domain.dto.CreatePositionRequest;
import com.gabrielluciano.employeeservice.domain.model.Position;
import com.gabrielluciano.employeeservice.domain.service.PositionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("positions")
@RequiredArgsConstructor
@Slf4j
public class PositionResource {

    private final PositionService service;

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody @Valid CreatePositionRequest body) {
        log.info("Received save position request");
        Position position = service.save(body);
        log.info("Successfully created position with id '{}'", position.getId());
        return ResponseEntity.created(createLocation(position)).build();
    }

    private URI createLocation(Position position) {
        return ServletUriComponentsBuilder.fromCurrentRequest()
                .pathSegment("{id}")
                .buildAndExpand(position.getId())
                .toUri();
    }
}
