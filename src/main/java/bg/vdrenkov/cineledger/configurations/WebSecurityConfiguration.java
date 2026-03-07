package bg.vdrenkov.cineledger.configurations;

import bg.vdrenkov.cineledger.jwt.JwtRequestFilter;
import bg.vdrenkov.cineledger.utils.constants.JwtConstants;
import bg.vdrenkov.cineledger.utils.constants.RoleConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import jakarta.servlet.http.HttpServletResponse;

import java.util.Arrays;

@EnableWebSecurity
@Configuration
public class WebSecurityConfiguration {

  private static final String[] AUTH_PATH = {
    "/csrf",
    "/login",
    "/registration",
    "/logout",
    "/password-recovery"
  };

  private static final String[] GUEST_GET_LIST = {
    "/categories.*",
    "/cinemas.*",
    "/cinemas/\\d/halls",
    "/cinemas/\\d/reviews",
    "/items.*",
    "/movies.*",
    "/categories/\\d/movies",
    "/programs.*",
    "/cinemas/\\d/programs",
    "/programs/\\d/projections",
    "/movies/\\d/projections",
    "/movies/\\d/reviews",
    "/projections(\\?.*|\\z)"
  };

  private static final String[] USER_LIST = {
    "/reviews/\\d.*",
    "/cinemas/\\d/reviews",
    "/movies/\\d/reviews",
    "/users/\\d/orders",
    "/users\\?username=.*",
    "/users\\?email=.*",
    "/users/\\d.*"
  };

  private static final String[] VENDOR_LIST = {
    "/items.*",
    "/items/\\d.*",
    "/discounts.*",
    "/discounts/\\d.*",
    "/orders.*",
    "/orders/\\d.*",
    "/tickets.*",
    "/projections/\\d/tickets.*"
  };

  private static final String[] ADMIN_LIST = {
    "/categories/\\d.*",
    "/cinemas/\\d.*",
    "/halls.*",
    "/halls/\\d.*",
    "/movies/\\d.*",
    "/programs/\\d.*",
    "/projections/\\d.*",
    "/reports.*",
    "/roles.*",
    "/roles/\\d.*",
    "/users\\?roleName=.*",
    "/users\\?joinDate=.*",
    "/admins.*",
    "/admins/\\d.*"
  };

  private static final String LOGOUT_URL = "/logout";

  private final JwtRequestFilter jwtRequestFilter;

  @Autowired
  public WebSecurityConfiguration(final JwtRequestFilter jwtRequestFilter) {
    this.jwtRequestFilter = jwtRequestFilter;
  }

  @Bean
  SecurityFilterChain securityFilterChain(final HttpSecurity httpSecurity) throws Exception {
    httpSecurity
      .csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
      .authorizeHttpRequests(auth -> auth
        .requestMatchers(AUTH_PATH).permitAll()
        .requestMatchers(regexMatchers(HttpMethod.GET, GUEST_GET_LIST)).permitAll()
        .requestMatchers(regexMatchers(null, USER_LIST))
        .hasAnyAuthority(RoleConstants.DEFAULT_USER_ROLE, RoleConstants.DEFAULT_VENDOR_ROLE,
                         RoleConstants.DEFAULT_ADMIN_ROLE)
        .requestMatchers(regexMatchers(null, VENDOR_LIST))
        .hasAnyAuthority(RoleConstants.DEFAULT_VENDOR_ROLE, RoleConstants.DEFAULT_ADMIN_ROLE)
        .requestMatchers(regexMatchers(null, GUEST_GET_LIST))
        .hasAuthority(RoleConstants.DEFAULT_ADMIN_ROLE)
        .requestMatchers(regexMatchers(null, ADMIN_LIST))
        .hasAuthority(RoleConstants.DEFAULT_ADMIN_ROLE)
        .anyRequest()
        .authenticated())
      .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
      .logout(logout -> logout
        .logoutUrl(LOGOUT_URL)
        .deleteCookies(JwtConstants.JWT_COOKIE_NAME)
        .logoutSuccessHandler((request, response, authentication) -> response.setStatus(HttpServletResponse.SC_OK)));
    return httpSecurity.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(final AuthenticationConfiguration authenticationConfiguration) throws
    Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  private static RequestMatcher[] regexMatchers(final HttpMethod httpMethod, final String[] patterns) {
    final String method = httpMethod == null ? null : httpMethod.name();
    return Arrays.stream(patterns)
                 .map(pattern -> new RegexRequestMatcher(pattern, method))
                 .toArray(RequestMatcher[]::new);
  }
}


