package DeliveryNow.Api.application.services;

import DeliveryNow.Api.application.services.dtos.LoginRequest;
import DeliveryNow.Api.application.services.dtos.LoginResponse;
import DeliveryNow.Api.application.services.dtos.UserEntityRequest;
import DeliveryNow.Api.application.services.dtos.UserEntityResponse;
import DeliveryNow.Api.domain.entities.UserEntity;
import DeliveryNow.Api.domain.interfaces.UserEntityRepository;
import DeliveryNow.Api.infrastructure.adapters.out.repositories.userEntity.JpaUserEntity;
import DeliveryNow.Api.infrastructure.config.jwt.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserEntityServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private TokenService tokenService;
    @Mock
    private UserEntityRepository userEntityRepository;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    private UserEntityService service;

    @BeforeEach
    void setUp() {
        service = new UserEntityService(authenticationManager, tokenService, userEntityRepository, encoder);
    }

    @Test
    void createUserStoresBcryptHashInsteadOfPlainPassword() {
        when(userEntityRepository.getUserEntityByEmail("ana@deliverynow.dev")).thenReturn(null);
        when(userEntityRepository.createUserEntity(any())).thenAnswer(inv -> inv.getArgument(0));

        UserEntityResponse response = service.createUser(
                new UserEntityRequest("ana@deliverynow.dev", "Ana", "Silva", "s3cret!"));

        ArgumentCaptor<UserEntity> saved = ArgumentCaptor.forClass(UserEntity.class);
        verify(userEntityRepository).createUserEntity(saved.capture());
        assertThat(saved.getValue().getPassword()).isNotEqualTo("s3cret!");
        assertThat(encoder.matches("s3cret!", saved.getValue().getPassword())).isTrue();

        assertThat(response).isEqualTo(new UserEntityResponse("ana@deliverynow.dev", "Ana", "Silva"));
    }

    @Test
    void createUserRejectsDuplicatedEmail() {
        when(userEntityRepository.getUserEntityByEmail("ana@deliverynow.dev")).thenReturn(new JpaUserEntity());

        assertThatThrownBy(() -> service.createUser(
                new UserEntityRequest("ana@deliverynow.dev", "Ana", "Silva", "s3cret!")))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("ana@deliverynow.dev");

        verify(userEntityRepository, never()).createUserEntity(any());
    }

    @Test
    void loginReturnsTokenAndUserId() {
        JpaUserEntity principal = new JpaUserEntity();
        principal.setId(42L);
        principal.setEmail("ana@deliverynow.dev");
        when(authenticationManager.authenticate(any()))
                .thenReturn(new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities()));
        when(tokenService.generateToken(principal)).thenReturn("jwt-token");

        LoginResponse response = service.login(new LoginRequest("ana@deliverynow.dev", "s3cret!"));

        assertThat(response).isEqualTo(new LoginResponse("jwt-token", 42L));
        ArgumentCaptor<UsernamePasswordAuthenticationToken> attempt =
                ArgumentCaptor.forClass(UsernamePasswordAuthenticationToken.class);
        verify(authenticationManager).authenticate(attempt.capture());
        assertThat(attempt.getValue().getPrincipal()).isEqualTo("ana@deliverynow.dev");
        assertThat(attempt.getValue().getCredentials()).isEqualTo("s3cret!");
    }

    @Test
    void loginPropagatesBadCredentials() {
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("bad"));

        assertThatThrownBy(() -> service.login(new LoginRequest("ana@deliverynow.dev", "wrong")))
                .isInstanceOf(BadCredentialsException.class);
        verifyNoInteractions(tokenService);
    }
}
