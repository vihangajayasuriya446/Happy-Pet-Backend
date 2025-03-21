package com.example.happypet.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Enable CORS
                .csrf(csrf -> csrf.disable()) // Disable CSRF
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("api/auth/register", "api/auth/authenticate",
                                "api/auth/register-admin", "api/v1/getusers",
                                "api/v1/updateowner/{id}",
                                // Pet Inquiry Controller Endpoints
                                "api/v1/inquiries", "api/v1/inquiries/{id}", "api/v1/inquiries",
                                "api/v1/inquiries/{id}", "api/v1/inquiries/{id}/status",
                                "api/v1/inquiries/email/{email}", "api/v1/inquiries/pet/{petId}",
                                "api/v1/inquiries/admin/dashboard/raw", "api/v1/inquiries/admin/dashboard",
                                "api/v1/inquiries/create",

                                // Contact Inquiry Controller Endpoints
                                "api/v1/contact-inquiries", "api/v1/contact-inquiries/{id}",
                                "api/v1/contact-inquiries/email/{email}", "api/v1/contact-inquiries/create",

                                // Cart Controller Endpoints
                                "api/v1/cart", "api/v1/cart/{cartId}", "api/v1/cart/user/{userId}",
                                "api/v1/cart/add", "api/v1/cart/update/{cartId}", "api/v1/cart/remove/{cartId}",

                                // Pet Controller Endpoints
                                "api/v1/pets", "api/v1/pets/{id}", "api/v1/pets/category/{category}",
                                "api/v1/pets/add", "api/v1/pets/update/{id}", "api/v1/pets/delete/{id}","/api/vi/pets/images/{fileName}"//
                        ).permitAll() // Public endpoints
                        .requestMatchers("api/v1/getfetchedusers","api/v1/addowner").hasAnyRole("USER", "ADMIN") // Allow both USER and ADMIN
                        .requestMatchers("/api/v1/adduser","api/v1/adduser",
                                "api/v1/updateuser", "api/v1/deleteuser/{userId}", "api/v1/getowners",
                                "api/v1/deleteowner/{ownerId}" ).hasRole("ADMIN") // Only ADMIN can access these

                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Stateless sessions
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
