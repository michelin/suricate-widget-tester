package io.suricate.widget.tester.configuration;

import io.suricate.widget.tester.properties.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.util.CollectionUtils;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * Global security configuration
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    /**
     * Application properties from properties file
     */
    private final ApplicationProperties applicationProperties;

    /**
     * Constructor
     *
     * @param applicationProperties Application properties
     */
    @Autowired
    public SecurityConfiguration(ApplicationProperties applicationProperties) {
      this.applicationProperties = applicationProperties;
    }

  /**
   * Apply the HTTP configuration
   *
   * @param http The HTTP security entity
   * @throws Exception Triggered exception during the configuration
   */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http
          .csrf().disable()
          .exceptionHandling()
          .and()
          .headers()
          .frameOptions().disable()
          .and()
          .sessionManagement()
          .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
          .and()
          .anonymous()
          .and()
          .authorizeRequests()
          .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
          .antMatchers("/api/**").permitAll();
    }

   /**
     * Configure the CORS filters
     */
    @Bean
    public CorsFilter corsFilter() {
      UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

      if (!CollectionUtils.isEmpty(applicationProperties.cors.getAllowedOrigins())) {
        source.registerCorsConfiguration("/api/**", applicationProperties.cors);
      }

      return new CorsFilter(source);
    }
}
