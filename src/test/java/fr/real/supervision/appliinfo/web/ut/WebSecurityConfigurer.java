package fr.real.supervision.appliinfo.web.ut;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        
        BCryptPasswordEncoder encoder = passwordEncoder();
        
        auth.inMemoryAuthentication()
            .passwordEncoder(encoder)
            .withUser("username")
            .password(encoder.encode("password"))
            .roles("USER");
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}