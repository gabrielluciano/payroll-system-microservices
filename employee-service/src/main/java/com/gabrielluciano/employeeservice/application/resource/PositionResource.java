package com.gabrielluciano.employeeservice.application.resource;

import com.gabrielluciano.employeeservice.application.resource.exception.StandardError;
import com.gabrielluciano.employeeservice.domain.dto.CreatePositionRequest;
import com.gabrielluciano.employeeservice.domain.dto.PositionResponse;
import com.gabrielluciano.employeeservice.domain.service.PositionService;
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

@Tag(name = "Position", description = "Work position resource")
@RestController
@RequestMapping("positions")
@RequiredArgsConstructor
@Slf4j
public class PositionResource {

    private final PositionService service;

    @Operation(summary = "List work positions", description = "Returns a list of work positions")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<PositionResponse>> list() {
        return ResponseEntity.ok(service.list());
    }

    @Operation(summary = "Save work position", description = "Creates a new work position")
    @ApiResponse(responseCode = "201", description = "Successfully created")
    @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> save(@RequestBody @Valid CreatePositionRequest body) {
        log.info("Received save position request");
        PositionResponse position = service.save(body);
        log.info("Successfully created position with id '{}'", position.id());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
