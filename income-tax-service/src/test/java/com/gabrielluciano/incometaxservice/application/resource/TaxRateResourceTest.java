package com.gabrielluciano.incometaxservice.application.resource;

import com.gabrielluciano.incometaxservice.config.TestcontainersConfiguration;
import com.gabrielluciano.incometaxservice.domain.dto.CreateTaxRateRequest;
import com.gabrielluciano.incometaxservice.domain.model.TaxRate;
import com.gabrielluciano.incometaxservice.infra.repository.TaxRateRepository;
import com.gabrielluciano.incometaxservice.util.JsonUtils;
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

import static java.math.BigDecimal.valueOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc
class TaxRateResourceTest {

    @Autowired
    private TaxRateRepository repository;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("Should save tax rate")
    void shouldSaveTaxRate() throws Exception {
        var request = new CreateTaxRateRequest(valueOf(1000.0), valueOf(2000.0), valueOf(0.02), valueOf(30.0));

        mockMvc.perform(post("/income/tax-rate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.asJsonString(request)))
                .andDo(print())
                .andExpect(status().isCreated());

        assertThat(repository.count()).isOne();
    }

    @Test
    @DisplayName("Should not save tax rate with duplicated minimum salary threshold")
    void shouldNotSaveTaxRateWithDuplicatedMinimumSalaryThreshold() throws Exception {
        TaxRate taxRate = new TaxRate(valueOf(1000.00), valueOf(1500.00), valueOf(0.02), valueOf(30.0));
        repository.saveAndFlush(taxRate);

        var request = new CreateTaxRateRequest(
                taxRate.getMinimumSalaryThreshold(),
                taxRate.getMaximumSalaryThreshold().add(valueOf(1.0)), // Add 1.0 to be different from taxRate
                valueOf(0.02),
                valueOf(30.0));
        mockMvc.perform(post("/income/tax-rate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.asJsonString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        assertThat(repository.count()).isOne();
    }

    @Test
    @DisplayName("Should not save tax rate with duplicated maximum salary threshold")
    void shouldNotSaveTaxRateWithDuplicatedMaximumSalaryThreshold() throws Exception {
        TaxRate taxRate = new TaxRate(valueOf(1000.00), valueOf(1500.00), valueOf(0.02), valueOf(30.0));
        repository.saveAndFlush(taxRate);

        var request = new CreateTaxRateRequest(
                taxRate.getMinimumSalaryThreshold().add(valueOf(1.0)), // Add 1.0 to be different from taxRate
                taxRate.getMaximumSalaryThreshold(),
                valueOf(0.02),
                valueOf(30.0));
        mockMvc.perform(post("/income/tax-rate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.asJsonString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        assertThat(repository.count()).isOne();
    }

    @Test
    @DisplayName("Should not save tax rate with null attributes")
    void shouldNotSaveTaxRateWithNullAttributes() throws Exception {
        var request = new CreateTaxRateRequest(null, null, null, null);

        mockMvc.perform(post("/income/tax-rate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.asJsonString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("minimumSalaryThreshold")))
                .andExpect(jsonPath("$.message", containsString("maximumSalaryThreshold")))
                .andExpect(jsonPath("$.message", containsString("rate")))
                .andExpect(jsonPath("$.message", containsString("deduction")));

        assertThat(repository.count()).isZero();
    }

    @Test
    @DisplayName("Should not save tax rate with negative attributes")
    void shouldNotSaveTaxRateWithNegativeAttributes() throws Exception {
        var request = new CreateTaxRateRequest(valueOf(-100.0), valueOf(-200.0), valueOf(-0.05), valueOf(-10.0));

        mockMvc.perform(post("/income/tax-rate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.asJsonString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("minimumSalaryThreshold")))
                .andExpect(jsonPath("$.message", containsString("maximumSalaryThreshold")))
                .andExpect(jsonPath("$.message", containsString("rate")))
                .andExpect(jsonPath("$.message", containsString("deduction")));

        assertThat(repository.count()).isZero();
    }

    @Test
    @DisplayName("Should not save tax rate when minimumSalaryThreshold is not less than maximumSalaryThreshold")
    void shouldNotSaveTaxRateWhenMinimumSalaryThresholdIsNotLessThanMaximumSalaryThreshold() throws Exception {
        var request1 = new CreateTaxRateRequest(valueOf(100.0), valueOf(100.0), valueOf(0.05), valueOf(10.0));
        mockMvc.perform(post("/income/tax-rate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.asJsonString(request1)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("invalid thresholds")));
        assertThat(repository.count()).isZero();

        var request2 = new CreateTaxRateRequest(valueOf(200.0), valueOf(100.0), valueOf(0.05), valueOf(10.0));
        mockMvc.perform(post("/income/tax-rate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.asJsonString(request2)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("invalid thresholds")));
        assertThat(repository.count()).isZero();
    }

    @Test
    @DisplayName("Should not save tax rate with rate greater than 1.0")
    void shouldNotSaveTaxRateWithRateGreaterThan1dot0() throws Exception {
        var request = new CreateTaxRateRequest(valueOf(100.0), valueOf(200.0), valueOf(10.05), valueOf(10.0));

        mockMvc.perform(post("/income/tax-rate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.asJsonString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("rate")));

        assertThat(repository.count()).isZero();
    }

    @Test
    @DisplayName("Should return list of tax rates")
    void shouldReturnListOfTaxRates() throws Exception {
        repository.saveAllAndFlush(List.of(
                new TaxRate(null, valueOf(0.00), valueOf(1000.00), valueOf(0.02), valueOf(0.00)),
                new TaxRate(null, valueOf(2001.00), valueOf(3000.00), valueOf(0.07), valueOf(100.00)),
                new TaxRate(null, valueOf(1001.00), valueOf(2000.00), valueOf(0.05), valueOf(50.00))
        ));

        mockMvc.perform(get("/income/tax-rate"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", equalTo(3)))
                .andExpect(jsonPath("$.[0].minimumSalaryThreshold", equalTo(0.00)))
                .andExpect(jsonPath("$.[1].minimumSalaryThreshold", equalTo(1001.00)))
                .andExpect(jsonPath("$.[2].minimumSalaryThreshold", equalTo(2001.00)));
    }

    @Test
    @DisplayName("Should delete tax rate")
    void shouldDeleteTaxRate() throws Exception {
        TaxRate taxRate = repository.saveAndFlush(new TaxRate(null, valueOf(1001.00), valueOf(2000.00), valueOf(0.05), valueOf(100.00)));
        assertThat(repository.count()).isOne();

        mockMvc.perform(delete("/income/tax-rate/{id}", taxRate.getId()))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertThat(repository.count()).isZero();
    }

    @Test
    @DisplayName("Should return not found when deleting non existent tax rate")
    void shouldReturnNotFoundWhenDeletingNonExistentTaxRate() throws Exception {
        long id = 2L;

        mockMvc.perform(delete("/income/tax-rate/{id}", id))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.path", equalTo("/income/tax-rate/" + id)))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.NOT_FOUND.value())))
                .andExpect(jsonPath("$.error", equalTo("Entity Not Found")))
                .andExpect(jsonPath("$.message", containsString("TaxRate")));
    }
}
