package com.gabrielluciano.employeeservice.application.resource;

import com.gabrielluciano.employeeservice.config.TestcontainersConfiguration;
import com.gabrielluciano.employeeservice.domain.dto.CreatePositionRequest;
import com.gabrielluciano.employeeservice.domain.model.Position;
import com.gabrielluciano.employeeservice.infra.repository.PositionRepository;
import com.gabrielluciano.employeeservice.util.JsonUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.gabrielluciano.employeeservice.util.TestUtils.executeConcurrently;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.StringContains.containsString;
import static org.hamcrest.text.MatchesPattern.matchesPattern;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestcontainersConfiguration.class)
@AutoConfigureMockMvc
class PositionResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PositionRepository positionRepository;

    @BeforeEach
    void setUp() {
        positionRepository.deleteAll();
    }

    @Test
    @DisplayName("Should save position")
    void shouldSavePosition() throws Exception {
        CreatePositionRequest createPositionRequest = new CreatePositionRequest("Software Developer");

        mockMvc.perform(post("/positions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.asJsonString(createPositionRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", matchesPattern("http:\\/\\/.+\\/positions\\/[0-9]+")));
    }

    @Test
    @DisplayName("Should not save duplicated positions")
    void shouldNotSaveDuplicatedPosition() throws Exception {
        CreatePositionRequest createPositionRequest = new CreatePositionRequest("Software Developer");
        positionRepository.save(createPositionRequest.toModel());

        mockMvc.perform(post("/positions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.asJsonString(createPositionRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.path", equalTo("/positions")))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.error", equalTo("Data Integrity Violation")))
                .andExpect(jsonPath("$.message", containsString("duplicate")));

        assertThat(positionRepository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("Should not save empty name")
    void shouldNotSaveEmptyName() throws Exception {
        CreatePositionRequest createPositionRequest = new CreatePositionRequest("");

        mockMvc.perform(post("/positions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.asJsonString(createPositionRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.path", equalTo("/positions")))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.error", equalTo("Constraint Violation")))
                .andExpect(jsonPath("$.message", containsString("name")));
    }

    @Test
    @DisplayName("Should not save duplicated positions on race condition")
    void shouldNotSaveDuplicatedPositionsOnRaceCondition() throws Exception {
        CreatePositionRequest createPositionRequest = new CreatePositionRequest("Software Developer");

        int numberOfExecutions = executeConcurrently(10, () -> mockMvc.perform(post("/positions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.asJsonString(createPositionRequest))));

        assertThat(numberOfExecutions).isEqualTo(10);
        assertThat(positionRepository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("Should return list of positions")
    void shouldReturnListOfPositions() throws Exception {
        positionRepository.saveAllAndFlush(List.of(
                new Position(null, "Software Developer"),
                new Position(null, "Business Analyst")
        ));

        mockMvc.perform(get("/positions"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", equalTo(2)));
    }
}
