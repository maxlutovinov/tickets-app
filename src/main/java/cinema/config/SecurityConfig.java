package cinema.config;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        final String[] adminPaths =
                {"/cinema-halls", "/movies", "/movie-sessions/**", "/users/by-email"};
        final String[] adminAndUserPaths =
                {"/cinema-halls", "/movies", "/movie-sessions/available"};
        final String[] userPaths = {"/orders/**", "/shopping-carts/**"};
        http
                .authorizeRequests()
                .antMatchers("/register").permitAll()
                .antMatchers(HttpMethod.GET, adminAndUserPaths).hasAnyRole("ADMIN", "USER")
                .antMatchers(adminPaths).hasRole("ADMIN")
                .antMatchers(userPaths).hasRole("USER")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .permitAll()
                .and()
                .httpBasic()
                .and()
                .csrf().disable();
    }
}
