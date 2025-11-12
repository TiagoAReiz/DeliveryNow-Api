package DeliveryNow.Api.application.services;

import DeliveryNow.Api.application.mappers.UserEntityMapper;
import DeliveryNow.Api.application.services.dtos.LoginRequest;
import DeliveryNow.Api.application.services.dtos.LoginResponse;
import DeliveryNow.Api.application.services.dtos.UserEntityRequest;
import DeliveryNow.Api.application.services.dtos.UserEntityResponse;
import DeliveryNow.Api.application.useCases.UserEntityUseCases;
import DeliveryNow.Api.domain.entities.UserEntity;
import DeliveryNow.Api.domain.interfaces.UserEntityRepository;
import DeliveryNow.Api.infrastructure.adapters.out.repositories.userEntity.JpaUserEntity;
import DeliveryNow.Api.infrastructure.config.jwt.TokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserEntityService implements UserEntityUseCases {
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UserEntityRepository userEntityRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserEntityService(AuthenticationManager authenticationManager, TokenService tokenService, UserEntityRepository userEntityRepository,  BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.userEntityRepository = userEntityRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public UserEntityResponse createUser(UserEntityRequest user) {
        UserEntity userEntity = UserEntityMapper.toUserEntity(user);
        userEntity.setPassword(bCryptPasswordEncoder.encode(userEntity.getPassword()));
        return UserEntityMapper.toUserEntityResponse(userEntityRepository.createUserEntity(userEntity));
    }


    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password());
        try {
            var auth = this.authenticationManager.authenticate(usernamePassword);
            var token = tokenService.generateToken((JpaUserEntity)auth.getPrincipal());
            return new LoginResponse(token, ((JpaUserEntity) auth.getPrincipal()).getId());
        }catch (Exception e){
            throw new UsernameNotFoundException(e.getMessage());
        }
    }
}
