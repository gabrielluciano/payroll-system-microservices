package com.gabrielluciano.workattendanceservice.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkAttendanceRecordId implements Serializable {

    @Column(nullable = false)
    @NotBlank
    @CPF
    private String employeeCpf;

    @Column(nullable = false)
    @NotNull
    @Min(1970)
    private Integer year;

    @Column(nullable = false)
    @NotNull
    @Min(1)
    @Max(12)
    private Integer month;

    @Override
    public String toString() {
        return "WorkAttendanceRecordId{" +
                "employeeCpf='" + employeeCpf + '\'' +
                ", year=" + year +
                ", month=" + month +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkAttendanceRecordId that = (WorkAttendanceRecordId) o;
        return Objects.equals(employeeCpf, that.employeeCpf) && Objects.equals(year, that.year) && Objects.equals(month, that.month);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeCpf, year, month);
    }
}
