package com.barabanov.mandatory.model.dbms.controller.rest.confuguration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SecurityConfiguration
{

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> {
                    request.requestMatchers("api/v1/column")
                            .hasAnyAuthority("SUPER_USER", "ADMIN");
                    request.requestMatchers("api/v1/db/create")
                            .hasAnyAuthority("ADMIN");
                    request.requestMatchers("api/v1/db/security/*")
                            .hasAnyAuthority("SUPER_USER", "ADMIN");
                    request.requestMatchers("api/v1/table")
                            .hasAnyAuthority("SUPER_USER", "ADMIN");
                    request.requestMatchers("api/v1/tuple/insert")
                            .hasAnyAuthority("USER", "SUPER_USER", "ADMIN");
                    request.requestMatchers("api/v1/tuple/security/lvl")
                            .hasAnyAuthority("SUPER_USER", "ADMIN");
                    request.requestMatchers("api/v1/tuple/delete")
                            .hasAnyAuthority("SUPER_USER", "ADMIN");
                    request.requestMatchers("api/v1/value")
                            .hasAnyAuthority("SUPER_USER", "ADMIN");
                    request.anyRequest().authenticated();

                })
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


}
