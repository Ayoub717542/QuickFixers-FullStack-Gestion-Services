package com.example.QuickFixersBackend.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.example.QuickFixersBackend.repository.UserRepository;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {
    private  final UserRepository userRepository;
    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public UserDetailsService userDetailsService(){
        return  username ->  userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("user not found"));
    }
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }
    @Bean
    public AuthenticationProvider authenticationProvider(
            UserDetailsService userDetailsService,
            BCryptPasswordEncoder passwordEncoder){
        DaoAuthenticationProvider authenticationProvider= new DaoAuthenticationProvider(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }


}
