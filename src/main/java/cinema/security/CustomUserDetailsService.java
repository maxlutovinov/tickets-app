package cinema.security;

import static org.springframework.security.core.userdetails.User.withUsername;

import cinema.model.User;
import cinema.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserService userService;

    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userService.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("User with email " + email + " not found"));
        return withUsername(email)
                .password(user.getPassword())
                .roles(user.getRoles().stream()
                        .map(role -> role.getRoleName().name())
                        .toArray(String[]::new))
                .build();
    }
}
