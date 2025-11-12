package DeliveryNow.Api.infrastructure.config.jwt;

import DeliveryNow.Api.domain.entities.UserEntity;
import DeliveryNow.Api.infrastructure.adapters.out.repositories.userEntity.JpaUserEntity;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.time.Instant;
import java.util.Date;

@Service
public class TokenService {
    @Value("${app.secret.key}")
    private String secretKey;

    public String generateToken(JpaUserEntity userEntity) {
        try{
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            return JWT.create()
                    .withIssuer("DeliveryNow")
                    .withSubject(userEntity.getEmail())
                    .withExpiresAt(Date.from(Instant.now().plusSeconds(3600)))
                    .sign(Algorithm.HMAC256(secretKey.getBytes()));
        }catch (JWTCreationException e){
            throw new RuntimeException("Error while generating token",e);
        }


    }
    public String validateToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey.getBytes());
            return JWT.require(algorithm)
                    .withIssuer("DeliveryNow")
                    .build()
                    .verify(token)
                    .getSubject();

        }
        catch (JWTVerificationException e){
            return "";
        }
    }
}