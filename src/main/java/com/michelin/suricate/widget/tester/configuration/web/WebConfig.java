/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.michelin.suricate.widget.tester.configuration.web;

import com.michelin.suricate.widget.tester.property.ApplicationProperties;
import com.michelin.suricate.widget.tester.security.ClientRoutingFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/** Web configuration. */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private ApplicationProperties applicationProperties;

    /**
     * Define the security filter chain.
     *
     * @param http The http security
     * @return The security filter chain
     * @throws Exception When an error occurred
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .cors(corsConfigurer -> corsConfigurer.configurationSource(corsConfiguration()))
                .sessionManagement(sessionManagementConfigurer ->
                        sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .headers(headersConfigurer ->
                        headersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .addFilterAfter(new ClientRoutingFilter(), BasicAuthenticationFilter.class)
                .authorizeHttpRequests(authorizeRequestsConfigurer -> authorizeRequestsConfigurer
                        .requestMatchers(CorsUtils::isPreFlightRequest)
                        .permitAll()
                        // Back-End
                        .requestMatchers("/api/**")
                        .permitAll()
                        // Front-End
                        .requestMatchers("/index.html", "/*.js", "/*.css", "/assets/**", "/media/**")
                        .permitAll()
                        // Default
                        .anyRequest()
                        .denyAll())
                .build();
    }

    /**
     * Define the CORS configuration.
     *
     * @return The CORS configuration
     */
    public UrlBasedCorsConfigurationSource corsConfiguration() {
        UrlBasedCorsConfigurationSource corsConfiguration = new UrlBasedCorsConfigurationSource();
        corsConfiguration.registerCorsConfiguration("/api/**", applicationProperties.getCors());
        return corsConfiguration;
    }
}
