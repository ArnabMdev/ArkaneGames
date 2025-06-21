package com.ArnabMdev.ArkaneGames.config;

import com.ArnabMdev.ArkaneGames.service.CustomOAuth2UserService;
import com.ArnabMdev.ArkaneGames.service.MongoUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    @Autowired
    private MongoUserDetailsService mongoUserDetailsService;

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:4200",  // Angular app
                "https://oauth.pstmn.io"  // Postman OAuth redirect
        ));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf
                        // Disable CSRF for API endpoints but keep it for web pages
                        .ignoringRequestMatchers("/api/**")
                )
                // Configure authorization for web pages and APIs separately
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.OPTIONS, "/api/**").permitAll() // Allow preflight requests
                        .requestMatchers("/api/**").authenticated()
                        .requestMatchers("/", "/login", "/error", "/webjars/**", "/register").permitAll()
                        .anyRequest().authenticated()
                )
                // Form-based login for web access
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .permitAll()
                        .defaultSuccessUrl("/", true)
                )
                // OAuth2 login configuration
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                        )
                        .successHandler((request, response, authentication) -> {
                            // Check if request comes from Postman
                            String referer = request.getHeader("Referer");
                            if (referer != null && referer.contains("oauth.pstmn.io")) {
                                // Get the access token
                                OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
                                OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                                        oauthToken.getAuthorizedClientRegistrationId(),
                                        oauthToken.getName()
                                );
                                String accessToken = client.getAccessToken().getTokenValue();

                                // Redirect back to Postman with token
                                response.sendRedirect("https://oauth.pstmn.io/v1/callback#access_token=" +
                                        accessToken + "&token_type=bearer");
                                return;
                            }

                            // Normal web flow - redirect to home page
                            response.sendRedirect("/");
                        }))
                        // Logout configuration
                        .logout(logout -> logout
                                .logoutUrl("/logout")
                                .logoutRequestMatcher(AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/logout"))
                                .logoutSuccessUrl("/")
                                .invalidateHttpSession(true)
                                .clearAuthentication(true)
                                .deleteCookies("JSESSIONID")
                                .permitAll()
                        )
                        .userDetailsService(mongoUserDetailsService);

        return http.build();
    }
}