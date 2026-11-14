package dev.vdrenkov.cineledger.configurations;

import dev.vdrenkov.cineledger.jwt.JwtRequestFilter;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.csrf.CsrfTokenRequestHandler;
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler;

import java.util.function.Supplier;

import static dev.vdrenkov.cineledger.utils.constants.JwtConstants.JWT_COOKIE_NAME;

@EnableWebSecurity
@Configuration
public class WebSecurityConfiguration {
    private static final String ADMIN = "ADMIN";
    private static final String VENDOR = "VENDOR";
    private static final String[] PUBLIC_READ = {
        "/categories", "/categories/{id}", "/categories/{id}/movies", "/cinemas", "/cinemas/{id}",
        "/cinemas/{id}/halls", "/cinemas/{id}/reviews", "/cinemas/{id}/programs", "/items", "/items/{id}",
        "/movies", "/movies/{id}", "/movies/{id}/reviews", "/movies/{id}/projections", "/programs", "/programs/{id}",
        "/programs/{id}/projections", "/projections", "/projections/{id}"
    };
    private static final String[] CUSTOMER_PATHS = {
        "/users/{id}", "/users/{id}/orders", "/users/{id}/movies/reviews", "/users/{id}/cinemas/reviews",
        "/reviews/{id}", "/cinemas/{id}/reviews", "/movies/{id}/reviews"
    };
    private static final String[] STAFF_PATHS = {
        "/orders", "/orders/{id}", "/items", "/items/{id}", "/discounts", "/discounts/{id}",
        "/tickets", "/projections/{id}/tickets"
    };

    private final JwtRequestFilter jwtRequestFilter;

    public WebSecurityConfiguration(final JwtRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    SecurityFilterChain securityFilterChain(final HttpSecurity httpSecurity) {
        httpSecurity
            .csrf(csrf -> csrf
                .csrfTokenRepository(new CookieCsrfTokenRepository())
                .csrfTokenRequestHandler(csrfTokenRequestHandler())
                .sessionAuthenticationStrategy(new NullAuthenticatedSessionStrategy()))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.GET, "/csrf").permitAll()
                .requestMatchers(HttpMethod.POST, "/login", "/users", "/password-reset-requests", "/password-resets").permitAll()
                .requestMatchers(HttpMethod.GET, PUBLIC_READ).permitAll()
                .requestMatchers(CUSTOMER_PATHS).authenticated()
                .requestMatchers(STAFF_PATHS).hasAnyAuthority(ADMIN, VENDOR)
                .requestMatchers("/users", "/admins/{id}", "/roles", "/roles/**", "/reports/**", "/halls", "/halls/{id}",
                    "/categories", "/categories/**", "/cinemas", "/cinemas/**", "/movies", "/movies/**",
                    "/programs", "/programs/**", "/projections", "/projections/{id}").hasAuthority(ADMIN)
                .anyRequest().denyAll())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
            .logout(logout -> logout
                .logoutUrl("/logout")
                .deleteCookies(JWT_COOKIE_NAME)
                .logoutSuccessHandler((_, response, _) -> response.setStatus(HttpServletResponse.SC_OK)));
        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public FilterRegistrationBean<JwtRequestFilter> jwtFilterRegistration() {
        final var registration = new FilterRegistrationBean<>(jwtRequestFilter);
        registration.setEnabled(false);
        return registration;
    }

    @Bean
    public AuthenticationManager authenticationManager(final AuthenticationConfiguration authenticationConfiguration) {
        return authenticationConfiguration.getAuthenticationManager();
    }

    private static CsrfTokenRequestHandler csrfTokenRequestHandler() {
        final var encodedTokens = new XorCsrfTokenRequestAttributeHandler();
        final var cookieTokens = new CsrfTokenRequestAttributeHandler();
        return new CsrfTokenRequestHandler() {
            @Override
            public void handle(final HttpServletRequest request, final HttpServletResponse response,
                final Supplier<CsrfToken> csrfToken) {
                encodedTokens.handle(request, response, csrfToken);
            }

            @Override
            public String resolveCsrfTokenValue(final HttpServletRequest request, final CsrfToken csrfToken) {
                final String encodedToken = encodedTokens.resolveCsrfTokenValue(request, csrfToken);
                return encodedToken != null ? encodedToken : cookieTokens.resolveCsrfTokenValue(request, csrfToken);
            }
        };
    }
}
