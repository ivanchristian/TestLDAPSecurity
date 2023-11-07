package com.example.LDAPSecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.example.LDAPSecurity.util.JwtTokenProvider;
import com.example.LDAPSecurity.util.LdapAuthenticationProvider;

@Configuration
@EnableWebSecurity
public class LDAPSecurityConfiguration extends WebSecurityConfigurerAdapter {
    private Environment env;

    public LDAPSecurityConfiguration(Environment env) {
        this.env = env;
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(new LdapAuthenticationProvider(env)).eraseCredentials(false);
        /*
         * .ldapAuthentication()
         * .passwordCompare()
         * .passwordEncoder(new BCryptPasswordEncoder())
         * .passwordAttribute("userPassword");
         */
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeRequests()
                .antMatchers("/auth-signin").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .csrf()
                .disable()
                .httpBasic();

    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public JwtTokenProvider provider() {
        return new JwtTokenProvider();
    }

}
