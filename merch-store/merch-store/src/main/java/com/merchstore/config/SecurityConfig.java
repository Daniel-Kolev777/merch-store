package com.merchstore.config;

import com.merchstore.security.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsService userDetailsService;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            UserDetailsService userDetailsService
    ) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public AuthenticationManager authenticationManager() {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(userDetailsService);

        provider.setPasswordEncoder(passwordEncoder());

        return new ProviderManager(provider);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(auth -> auth

                        // =====================================================
                        // AUTH - PUBLIC
                        // =====================================================

                        .requestMatchers(
                                "/api/auth/login",
                                "/api/auth/register",
                                "/api/auth/restore"
                        ).permitAll()


                        // =====================================================
                        // ADMIN - ORDERS
                        // Must be BEFORE public /api/orders/**
                        // =====================================================

                        .requestMatchers(
                                HttpMethod.PATCH,
                                "/api/orders/*/ship"
                        ).hasAuthority("ADMIN")


                        // =====================================================
                        // ADMIN - PRODUCTS
                        // =====================================================

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/products/admin",
                                "/api/products/admin/**"
                        ).hasAuthority("ADMIN")

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/products",
                                "/api/products/**"
                        ).hasAuthority("ADMIN")

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/products/**"
                        ).hasAuthority("ADMIN")

                        .requestMatchers(
                                HttpMethod.PATCH,
                                "/api/products/**"
                        ).hasAuthority("ADMIN")

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/products/**"
                        ).hasAuthority("ADMIN")


                        // =====================================================
                        // ADMIN - CATEGORIES
                        // =====================================================

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/categories",
                                "/api/categories/**"
                        ).hasAuthority("ADMIN")

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/categories/**"
                        ).hasAuthority("ADMIN")

                        .requestMatchers(
                                HttpMethod.PATCH,
                                "/api/categories/**"
                        ).hasAuthority("ADMIN")

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/categories/**"
                        ).hasAuthority("ADMIN")


                        // =====================================================
                        // ADMIN - PRODUCT IMAGES
                        // =====================================================

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/product_images/**"
                        ).hasAuthority("ADMIN")

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/product_images/**"
                        ).hasAuthority("ADMIN")

                        .requestMatchers(
                                HttpMethod.PATCH,
                                "/api/product_images/**"
                        ).hasAuthority("ADMIN")

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/product_images/**"
                        ).hasAuthority("ADMIN")


                        // =====================================================
                        // PUBLIC - CART
                        // Guest carts are supported
                        // =====================================================

                        .requestMatchers(
                                "/api/carts",
                                "/api/carts/**"
                        ).permitAll()


                        // =====================================================
                        // PUBLIC - ORDERS
                        // Guest checkout / guest order access
                        // Service handles ownership
                        // =====================================================

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/orders"
                        ).permitAll()

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/orders",
                                "/api/orders/**"
                        ).permitAll()


                        // =====================================================
                        // PUBLIC - PRODUCTS
                        // =====================================================

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/products",
                                "/api/products/**"
                        ).permitAll()


                        // =====================================================
                        // PUBLIC - CATEGORIES
                        // =====================================================

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/categories",
                                "/api/categories/**"
                        ).permitAll()


                        // =====================================================
                        // PUBLIC - PRODUCT IMAGES
                        // =====================================================

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/product_images",
                                "/api/product_images/**"
                        ).permitAll()


                        // =====================================================
                        // PUBLIC - ECONT
                        // Needed during checkout
                        // =====================================================

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/econt/offices",
                                "/api/econt/offices/**"
                        ).permitAll()


                        // =====================================================
                        // EVERYTHING ELSE
                        // User must be authenticated
                        // =====================================================

                        .anyRequest().authenticated()
                )

                .exceptionHandling(exception -> exception

                        .authenticationEntryPoint(
                                (request, response, authException) -> {

                                    response.setStatus(
                                            HttpServletResponse.SC_UNAUTHORIZED
                                    );

                                    response.setContentType(
                                            "application/json"
                                    );

                                    response.setCharacterEncoding(
                                            "UTF-8"
                                    );

                                    response.getWriter().write(
                                            """
                                            {
                                                "error": "Unauthorized",
                                                "message": "Authentication is required!"
                                            }
                                            """
                                    );
                                }
                        )

                        .accessDeniedHandler(
                                (request, response, accessDeniedException) -> {

                                    response.setStatus(
                                            HttpServletResponse.SC_FORBIDDEN
                                    );

                                    response.setContentType(
                                            "application/json"
                                    );

                                    response.setCharacterEncoding(
                                            "UTF-8"
                                    );

                                    response.getWriter().write(
                                            """
                                            {
                                                "error": "Forbidden",
                                                "message": "You do not have permission to access this resource!"
                                            }
                                            """
                                    );
                                }
                        )
                )

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }
}