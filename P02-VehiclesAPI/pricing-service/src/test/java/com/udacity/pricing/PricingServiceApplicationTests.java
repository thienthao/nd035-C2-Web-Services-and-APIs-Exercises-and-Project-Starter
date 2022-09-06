package com.udacity.pricing;

import com.udacity.pricing.domain.price.Price;
import com.udacity.pricing.service.PricingService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;

import static org.mockito.Mockito.mockStatic;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
public class PricingServiceApplicationTests {

    @Autowired
    private static MockMvc mockMvc;

    @BeforeAll
    static void init() {
        try (MockedStatic<PricingService> mocked = mockStatic(PricingService.class)) {
            mocked.when(() -> PricingService.getPrice(1L))
                    .thenReturn(new Price("USD", new BigDecimal("13779.96"), 1L));
        }
    }

    @Test
    public void contextLoads() {
    }

    @Test
    public void test_get_vehicle_price() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/services/price?vehicleId=1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("vehicleId").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("price").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("currency").value("USD"))
                .andReturn();
    }
}
