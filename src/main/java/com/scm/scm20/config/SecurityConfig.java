package com.scm.scm20.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.scm.scm20.services.impl.SecurityCustomUserDetailService;

@Configuration
public class SecurityConfig {

    // @Bean
    // public UserDetailsService userDetailsService() {

    // UserDetails admin = User.withDefaultPasswordEncoder()
    // .username("admin")
    // .password("admin123")
    // .roles("ADMIN")
    // .build();

    // return new InMemoryUserDetailsManager(admin);
    // }

    @Autowired
    private SecurityCustomUserDetailService userDetailService;

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        // URL Authorization with public and private access
        httpSecurity.authorizeHttpRequests(authz -> authz
                .requestMatchers("/user/**").authenticated()
                .anyRequest().permitAll());

        // form default login
        httpSecurity.formLogin(formLogin -> formLogin
                .loginPage("/login")
                .loginProcessingUrl("/authenticate")
                .successForwardUrl("/user/dashboard")
                .failureForwardUrl("/login?error=true")
                .usernameParameter("email")
                .passwordParameter("password")
        // .failureHandler(new AuthenticationFailureHandler() {

        // @Override
        // public void onAuthenticationFailure(HttpServletRequest request,
        // HttpServletResponse response,
        // AuthenticationException exception) throws IOException, ServletException {
        // String errorMessage = "Invalid username or password";

        // if (exception.getMessage().equalsIgnoreCase("User is disabled")) {
        // errorMessage = "Your account is disabled. Please contact support.";
        // } else if (exception.getMessage().equalsIgnoreCase("User account has
        // expired")) {
        // errorMessage = "Your account has expired. Please contact support.";
        // }

        // request.getSession().setAttribute("error", errorMessage);
        // response.sendRedirect("/login?error=true");
        // }
        // })
        // .successHandler(new AuthenticationSuccessHandler() {

        // @Override
        // public void onAuthenticationSuccess(HttpServletRequest request,
        // HttpServletResponse response,
        // org.springframework.security.core.Authentication authentication)
        // throws IOException, ServletException {
        // response.sendRedirect("/user/dashboard");
        // }
        // })
        );

        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        httpSecurity.logout(logoutForm -> {
            logoutForm
                    .logoutUrl("/do-logout")
                    .logoutSuccessUrl("/login?logout=true");
        });
        // httpSecurity.logout(logout -> logout
        // .logoutUrl("/perform_logout")
        // .logoutSuccessUrl("/login?logout=true"));
        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
