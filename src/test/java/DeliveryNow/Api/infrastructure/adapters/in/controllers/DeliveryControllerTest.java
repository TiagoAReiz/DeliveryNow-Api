package DeliveryNow.Api.infrastructure.adapters.in.controllers;

import DeliveryNow.Api.application.services.dtos.DeliveryResponse;
import DeliveryNow.Api.application.services.dtos.SearchRequest;
import DeliveryNow.Api.application.useCases.DeliveryUseCases;
import DeliveryNow.Api.domain.entities.enums.DeliveryStatus;
import DeliveryNow.Api.domain.entities.valueObjects.Address;
import DeliveryNow.Api.domain.interfaces.UserEntityRepository;
import DeliveryNow.Api.infrastructure.adapters.out.repositories.userEntity.JpaUserEntity;
import DeliveryNow.Api.infrastructure.config.SecurityConfig;
import DeliveryNow.Api.infrastructure.config.jwt.TokenService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Exercises the real security chain (SecurityConfig + AuthFilter) in front of the controller.
 */
@WebMvcTest(DeliveryController.class)
@Import(SecurityConfig.class)
class DeliveryControllerTest {

    private static final String VALID_TOKEN = "valid-token";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DeliveryUseCases deliveryUseCases;
    @MockitoBean
    private TokenService tokenService;
    @MockitoBean
    private UserEntityRepository userEntityRepository;
    @MockitoBean
    private UserDetailsService userDetailsService;

    private final DeliveryResponse delivery = new DeliveryResponse(1L, "Pedido #1",
            new Address("Curitiba", "80000-000", "Rua XV", "BR", "PR", "80000-000"),
            DeliveryStatus.PENDING, LocalDate.of(2030, 1, 1), null);

    @BeforeEach
    void authenticatedCourier() {
        JpaUserEntity courier = new JpaUserEntity();
        courier.setId(10L);
        courier.setEmail("courier@deliverynow.dev");
        when(tokenService.validateToken(VALID_TOKEN)).thenReturn("courier@deliverynow.dev");
        when(tokenService.validateToken("invalid-token")).thenReturn("");
        when(userEntityRepository.getUserEntityByEmail("courier@deliverynow.dev")).thenReturn(courier);
    }

    @Test
    void requestWithoutTokenIsForbidden() throws Exception {
        mockMvc.perform(get("/delivery")).andExpect(status().isForbidden());
        verifyNoInteractions(deliveryUseCases);
    }

    @Test
    void requestWithInvalidTokenIsForbiddenNotServerError() throws Exception {
        mockMvc.perform(get("/delivery").header("Authorization", "Bearer invalid-token"))
                .andExpect(status().isForbidden());
        verifyNoInteractions(deliveryUseCases);
    }

    @Test
    void listDeliveriesWithValidToken() throws Exception {
        when(deliveryUseCases.getAllDeliveries()).thenReturn(List.of(delivery));

        mockMvc.perform(get("/delivery").header("Authorization", "Bearer " + VALID_TOKEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].status").value("PENDING"))
                .andExpect(jsonPath("$[0].address.city").value("Curitiba"));
    }

    @Test
    void unknownDeliveryReturnsNotFound() throws Exception {
        when(deliveryUseCases.getDeliveryById(404L)).thenThrow(new EntityNotFoundException("nope"));

        mockMvc.perform(get("/delivery/404").header("Authorization", "Bearer " + VALID_TOKEN))
                .andExpect(status().isNotFound());
    }

    @Test
    void searchForwardsFiltersToUseCase() throws Exception {
        when(deliveryUseCases.search(new SearchRequest("curitiba", DeliveryStatus.LATE, 10L)))
                .thenReturn(List.of(delivery));

        mockMvc.perform(get("/delivery/search")
                        .param("search", "curitiba")
                        .param("status", "LATE")
                        .param("userId", "10")
                        .header("Authorization", "Bearer " + VALID_TOKEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void searchWithoutUserIdIsBadRequest() throws Exception {
        mockMvc.perform(get("/delivery/search").header("Authorization", "Bearer " + VALID_TOKEN))
                .andExpect(status().isBadRequest());
    }
}
