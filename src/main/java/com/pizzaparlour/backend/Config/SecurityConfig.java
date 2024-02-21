package com.pizzaparlour.backend.Config;

import com.pizzaparlour.backend.Util.JwtRequestFilter;
import com.pizzaparlour.backend.Util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

   @Autowired
   private CustomerInfo customerInfo;

   @Autowired
   private JwtUtil jwtUtil;

   @Autowired
   private JwtRequestFilter jwtRequestFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable).cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth.requestMatchers("/pizza-parlour/public/**")
                        .permitAll().requestMatchers("/pizza-parlour/admin/private/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/pizza-parlour/customer/private/**").hasAnyAuthority("ROLE_USER","ROLE_ADMIN")
                        .requestMatchers("/pizza-parlour/common/private/**").hasAnyAuthority("ROLE_USER","ROLE_ADMIN")
                        .anyRequest()
                        .authenticated())
                        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
                http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
                return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(customerInfo);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }
}
