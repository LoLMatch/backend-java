package com.lolmatch.calendar.service;

import com.lolmatch.calendar.audit.ApplicationAuditAware;
import com.lolmatch.calendar.dto.response.SuggestionResponse;
import com.lolmatch.calendar.model.UserEntity;
import com.lolmatch.calendar.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ApplicationAuditAware audit;

    public void save(UserEntity user) {
        userRepository.save(user);
    }

    public SuggestionResponse getSuggestions(Optional<String> snippet) {
        //todo: sugestie powinny być parami ;0

        final UUID currentUser = audit.getCurrentAuditor()
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("User has to be authenticated!"));

        return snippet.map(s -> new SuggestionResponse(userRepository.findByUsernameStartingWith(s)))
                .orElseGet(() -> new SuggestionResponse(userRepository.findAll().stream()
                        .filter(suggestion -> !suggestion.getId().equals(currentUser))
                        .toList())
                );
    }
}
