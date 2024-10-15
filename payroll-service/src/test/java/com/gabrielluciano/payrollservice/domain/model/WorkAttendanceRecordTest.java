package com.gabrielluciano.payrollservice.domain.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class WorkAttendanceRecordTest {

    @ParameterizedTest
    @CsvSource({
        "100, 80, 0.80000",
        "99, 30, 0.30303",
        "100, 0, 0.00000",
        "100, 100, 1.00000"
    })
    @DisplayName("Should calculate percentage correclty")
    void shouldCalculatePercentage(int expectedWorkingHours, int actualWorkingHours, String expectedPercentage) {
        WorkAttendanceRecord attendanceRecord = new WorkAttendanceRecord("", 2024, 10, expectedWorkingHours, actualWorkingHours);
        final var percentageOfHoursWorked = attendanceRecord.getPercentageOfHoursWorked();
        assertThat(percentageOfHoursWorked).isEqualByComparingTo(new BigDecimal(expectedPercentage));
    }
}
