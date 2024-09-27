package com.gabrielluciano.workattendanceservice.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkAttendanceRecordId implements Serializable {

    @Column(nullable = false)
    private String employeeCpf;
    @Column(nullable = false)
    private Integer year;
    @Column(nullable = false)
    private Integer month;
}
