package DeliveryNow.Api.infrastructure.adapters.in.controllers;

import DeliveryNow.Api.application.services.dtos.LoginRequest;
import DeliveryNow.Api.application.services.dtos.LoginResponse;
import DeliveryNow.Api.application.services.dtos.UserEntityResponse;
import DeliveryNow.Api.application.useCases.UserEntityUseCases;
import DeliveryNow.Api.domain.interfaces.UserEntityRepository;
import DeliveryNow.Api.infrastructure.config.SecurityConfig;
import DeliveryNow.Api.infrastructure.config.jwt.TokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserEntityController.class)
@Import(SecurityConfig.class)
class UserEntityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserEntityUseCases userEntityUseCases;
    @MockitoBean
    private TokenService tokenService;
    @MockitoBean
    private UserEntityRepository userEntityRepository;
    @MockitoBean
    private UserDetailsService userDetailsService;

    private static final String REGISTER_BODY = """
            {"email":"ana@deliverynow.dev","firstName":"Ana","lastName":"Silva","password":"s3cret!"}
            """;

    @Test
    void registerIsPublicAndReturnsCreatedUserWithoutPassword() throws Exception {
        when(userEntityUseCases.createUser(any()))
                .thenReturn(new UserEntityResponse("ana@deliverynow.dev", "Ana", "Silva"));

        mockMvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON).content(REGISTER_BODY))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("ana@deliverynow.dev"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void registerWithExistingEmailReturnsConflict() throws Exception {
        when(userEntityUseCases.createUser(any())).thenThrow(new IllegalStateException("exists"));

        mockMvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON).content(REGISTER_BODY))
                .andExpect(status().isConflict());
    }

    @Test
    void loginReturnsJwtAndUserId() throws Exception {
        when(userEntityUseCases.login(new LoginRequest("ana@deliverynow.dev", "s3cret!")))
                .thenReturn(new LoginResponse("jwt-token", 42L));

        mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"ana@deliverynow.dev\",\"password\":\"s3cret!\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"))
                .andExpect(jsonPath("$.id").value(42));
    }

    @Test
    void loginWithBadCredentialsReturnsUnauthorized() throws Exception {
        when(userEntityUseCases.login(any())).thenThrow(new BadCredentialsException("bad"));

        mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"ana@deliverynow.dev\",\"password\":\"wrong\"}"))
                .andExpect(status().isUnauthorized());
    }
}
