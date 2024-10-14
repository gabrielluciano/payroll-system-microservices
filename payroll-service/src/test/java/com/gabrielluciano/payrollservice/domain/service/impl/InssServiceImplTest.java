package com.gabrielluciano.payrollservice.domain.service.impl;

import static java.math.BigDecimal.valueOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.gabrielluciano.payrollservice.domain.dto.InssTaxRate;
import com.gabrielluciano.payrollservice.domain.service.InssService;
import com.gabrielluciano.payrollservice.domain.service.InssTaxService;

@ExtendWith(SpringExtension.class)
class InssServiceImplTest {

    @MockBean
    private InssTaxService taxService;

    private InssService inssServiceImpl;

    private final List<InssTaxRate> taxRates = List.of(
        new InssTaxRate(1L, valueOf(0.00),    valueOf(1412.00), valueOf(0.075)),
        new InssTaxRate(2L, valueOf(1412.01), valueOf(2666.68), valueOf(0.09)),
        new InssTaxRate(3L, valueOf(2666.69), valueOf(4000.03), valueOf(0.12)),
        new InssTaxRate(4L, valueOf(4000.04), valueOf(7786.02), valueOf(0.14))
    );

    @BeforeEach
    void setUp() {
        when(taxService.getTaxRates()).thenReturn(taxRates);
        inssServiceImpl = new InssServiceImpl(taxService);
    }

    @ParameterizedTest
    @CsvSource({
        // Between ranges
        "1000.00, 75.00",
        "2500.00, 203.82",
        "3000.00, 258.82",
        "5000.00, 518.82",
        "9000.00, 908.86",

        // Minimum Threshold
        "0.00, 0.00",
        "1412.01, 105.90",
        "2666.69, 218.82",
        "4000.04, 378.82",

        // Maximum Threshold
        "1412.00, 105.90",
        "2666.68, 218.82",
        "4000.03, 378.82",
        "7786.02, 908.86",
    })
    void testCalculateDiscount(String grossPay, String expectedTax) {
        var tax = inssServiceImpl.calculateDiscount(new BigDecimal(grossPay));
        assertThat(tax).isEqualByComparingTo(new BigDecimal(expectedTax));
    }
}
