package br.ifsp.hospital.security.auth;

import br.ifsp.hospital.exception.EntityAlreadyExistsException;
import br.ifsp.hospital.security.config.JwtService;
import br.ifsp.hospital.security.user.JpaUserRepository;
import br.ifsp.hospital.security.user.Role;
import br.ifsp.hospital.security.user.User;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthenticationService {

    private final JpaUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public RegisterUserResponse register(RegisterUserRequest request) {
        userRepository.findByEmail(request.email()).ifPresent(u -> {
            throw new EntityAlreadyExistsException("Email já cadastrado: " + request.email());
        });

        String encryptedPassword = passwordEncoder.encode(request.password());
        UUID id = UUID.randomUUID();

        User user = User.builder()
                .id(id)
                .name(request.name())
                .lastname(request.lastname())
                .email(request.email())
                .password(encryptedPassword)
                .role(Role.USER)
                .build();

        userRepository.save(user);
        return new RegisterUserResponse(id);
    }

    public AuthResponse authenticate(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );
        User user = userRepository.findByEmail(request.username()).orElseThrow();
        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }
}
