package com.gabrielluciano.workattendanceservice.infra.repository;

import com.gabrielluciano.workattendanceservice.config.TestcontainersConfiguration;
import com.gabrielluciano.workattendanceservice.domain.model.WorkAttendanceRecord;
import com.gabrielluciano.workattendanceservice.domain.model.WorkAttendanceRecordId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(TestcontainersConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class WorkAttendanceRepositoryTest {

    private static final String VALID_CPF_1 = "127.361.540-96";
    private static final String VALID_CPF_2 = "743.914.890-82";

    private WorkAttendanceRecord savedRecord = null;

    @Autowired
    private WorkAttendanceRecordRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();

        int year = 2024;
        int month = 10;
        int expectedWorkingHours = 180;
        int actualWorkingHours = 175;
        var id = new WorkAttendanceRecordId(VALID_CPF_1, year, month);
        savedRecord = repository.saveAndFlush(new WorkAttendanceRecord(id, expectedWorkingHours, actualWorkingHours));
    }

    @Test
    @DisplayName("findByRecordId should return WorkAttendanceRecord")
    void findByRecordId_ShouldReturnWorkAttendanceRecord() {
        var id = savedRecord.getRecordId();

        var recordFromDb = repository.findByRecordId(id);

        assertThat(recordFromDb).isNotEmpty();
        assertThat(recordFromDb.get().getRecordId()).isEqualTo(id);
        assertThat(recordFromDb.get().getExpectedWorkingHours()).isEqualTo(savedRecord.getExpectedWorkingHours());
        assertThat(recordFromDb.get().getActualWorkingHours()).isEqualTo(savedRecord.getActualWorkingHours());
    }

    @Test
    @DisplayName("findByRecordId should not find when different employeeCpf")
    void findByRecordId_ShouldNotFindWhenDifferentEmployeeCpf() {
        var idToSearch = new WorkAttendanceRecordId(VALID_CPF_2,
                savedRecord.getRecordId().getYear(), savedRecord.getRecordId().getYear());

        var recordFromDb = repository.findByRecordId(idToSearch);

        assertThat(recordFromDb).isEmpty();
    }

    @Test
    @DisplayName("findByRecordId should not find when different year")
    void findByRecordId_ShouldNotFindWhenDifferentYear() {
        var idToSearch = new WorkAttendanceRecordId(VALID_CPF_1, savedRecord.getRecordId().getYear() + 1, // sum 1 to make year different
                savedRecord.getRecordId().getMonth());

        var recordFromDb = repository.findByRecordId(idToSearch);

        assertThat(recordFromDb).isEmpty();
    }

    @Test
    @DisplayName("findByRecordId should not find when different month")
    void findByRecordId_ShouldNotFindWhenDifferentMonth() {
        var idToSearch = new WorkAttendanceRecordId(VALID_CPF_1, savedRecord.getRecordId().getYear(),
                savedRecord.getRecordId().getMonth() + 1); // sum 1 to make month different
        var recordFromDb = repository.findByRecordId(idToSearch);

        assertThat(recordFromDb).isEmpty();
    }
}
