package DeliveryNow.Api.infrastructure.config.jwt;

import DeliveryNow.Api.infrastructure.adapters.out.repositories.userEntity.JpaUserEntity;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

class TokenServiceTest {

    private static final String SECRET = "test-secret";

    private TokenService tokenService;

    @BeforeEach
    void setUp() {
        tokenService = new TokenService();
        ReflectionTestUtils.setField(tokenService, "secretKey", SECRET);
    }

    private static JpaUserEntity user(String email) {
        JpaUserEntity user = new JpaUserEntity();
        user.setEmail(email);
        return user;
    }

    @Test
    void generatedTokenValidatesBackToTheUserEmail() {
        String token = tokenService.generateToken(user("courier@deliverynow.dev"));

        assertThat(tokenService.validateToken(token)).isEqualTo("courier@deliverynow.dev");
    }

    @Test
    void generatedTokenHasIssuerAndExpiresInAboutOneHour() {
        String token = tokenService.generateToken(user("courier@deliverynow.dev"));

        var decoded = JWT.decode(token);
        assertThat(decoded.getIssuer()).isEqualTo("DeliveryNow");
        assertThat(decoded.getExpiresAtAsInstant())
                .isBetween(Instant.now().plusSeconds(3500), Instant.now().plusSeconds(3700));
    }

    @Test
    void tokenSignedWithAnotherSecretIsRejected() {
        String forged = JWT.create()
                .withIssuer("DeliveryNow")
                .withSubject("attacker@example.com")
                .sign(Algorithm.HMAC256("another-secret"));

        assertThat(tokenService.validateToken(forged)).isEmpty();
    }

    @Test
    void expiredTokenIsRejected() {
        String expired = JWT.create()
                .withIssuer("DeliveryNow")
                .withSubject("courier@deliverynow.dev")
                .withExpiresAt(Date.from(Instant.now().minusSeconds(60)))
                .sign(Algorithm.HMAC256(SECRET.getBytes()));

        assertThat(tokenService.validateToken(expired)).isEmpty();
    }

    @Test
    void tokenFromAnotherIssuerIsRejected() {
        String otherIssuer = JWT.create()
                .withIssuer("SomeoneElse")
                .withSubject("courier@deliverynow.dev")
                .sign(Algorithm.HMAC256(SECRET.getBytes()));

        assertThat(tokenService.validateToken(otherIssuer)).isEmpty();
    }

    @Test
    void garbageTokenIsRejected() {
        assertThat(tokenService.validateToken("not-a-jwt")).isEmpty();
    }
}
