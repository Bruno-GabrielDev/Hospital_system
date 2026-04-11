package br.ifsp.hospital.security.auth;

import br.ifsp.hospital.security.user.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthenticationInfoService {

    public UUID getAuthenticatedUserId() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated())
            throw new IllegalStateException("Requisição não autenticada.");
        User user = (User) authentication.getPrincipal();
        return user.getId();
    }
}
