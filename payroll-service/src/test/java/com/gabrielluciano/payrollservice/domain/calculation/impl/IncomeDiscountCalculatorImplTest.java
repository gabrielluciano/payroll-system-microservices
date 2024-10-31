package com.gabrielluciano.payrollservice.domain.calculation.impl;

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

import com.gabrielluciano.payrollservice.domain.calculation.IncomeDiscountCalculator;
import com.gabrielluciano.payrollservice.domain.dto.IncomeTaxRate;
import com.gabrielluciano.payrollservice.domain.provider.IncomeTaxRateProvider;

@ExtendWith(SpringExtension.class)
class IncomeDiscountCalculatorImplTest {

    @MockBean
    private IncomeTaxRateProvider taxRateProvider;

    private IncomeDiscountCalculator incomeDiscountCalculatorImpl;

    private final List<IncomeTaxRate> taxRates = List.of(
        new IncomeTaxRate(null, valueOf(0.00),    valueOf(2112.00), valueOf(0.00), valueOf(0.00)),
        new IncomeTaxRate(null, valueOf(2112.01), valueOf(2826.65), valueOf(0.075), valueOf(158.40)),
        new IncomeTaxRate(null, valueOf(2826.66), valueOf(3751.05), valueOf(0.15), valueOf(370.40)),
        new IncomeTaxRate(null, valueOf(3751.06), valueOf(4664.68), valueOf(0.225), valueOf(651.73)),
        new IncomeTaxRate(null, valueOf(4664.69), valueOf(10000000.00), valueOf(0.275), valueOf(884.96))
    );

    @BeforeEach
    void setUp() {
        when(taxRateProvider.getTaxRates()).thenReturn(taxRates);
        incomeDiscountCalculatorImpl = new IncomeDiscountCalculatorImpl(taxRateProvider);
    }

    @ParameterizedTest
    @CsvSource({
        // Between ranges
        "1000.00, 0.00",
        "2500.00, 29.1",
        "3000.00, 79.6",
        "4000.00, 248.27",
        "5000.00, 490.04",

        //Minimum Threshold
        "0.00, 0.00",
        "2112.01, 0.00",
        "2826.66, 53.60",
        "3751.06, 192.26",
        "4664.69, 397.83",

        // Maximum Threshold
        "2112.00, 0.00",
        "2826.65, 53.60",
        "3751.05, 192.26",
        "4664.68, 397.82",
    })
    void testCalculateDiscount(String grossPay, String expectedTax) {
        var tax = incomeDiscountCalculatorImpl.calculateDiscount(new BigDecimal(grossPay));
        assertThat(tax).isEqualByComparingTo(new BigDecimal(expectedTax));
    }
}
