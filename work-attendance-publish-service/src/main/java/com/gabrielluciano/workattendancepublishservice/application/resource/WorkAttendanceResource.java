package com.gabrielluciano.workattendancepublishservice.application.resource;

import com.gabrielluciano.workattendancepublishservice.application.resource.exception.StandardError;
import com.gabrielluciano.workattendancepublishservice.domain.dto.CreateWorkAttendanceRequest;
import com.gabrielluciano.workattendancepublishservice.domain.service.WorkAttendanceService;
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

@Tag(name = "Work Attendance", description = "Work Attendance Publish Resource")
@RestController
@RequestMapping("work-attendances")
@RequiredArgsConstructor
@Slf4j
public class WorkAttendanceResource {

    private final WorkAttendanceService service;

    @Operation(summary = "Post a new Work Attendance", description = "Post a new Work Attendance for a given cpf in the given month and year")
    @ApiResponse(responseCode = "201", description = "Successfully posted Work Attendance")
    @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
    @ApiResponse(responseCode = "404", description = "Employee not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
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
