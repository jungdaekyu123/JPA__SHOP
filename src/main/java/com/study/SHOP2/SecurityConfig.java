package com.study.SHOP2;

import com.study.SHOP2.User.CustomUserDetailsService;

import com.study.SHOP2.session.LoginSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .securityContext(context -> context.requireExplicitSave(false))
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .ignoringRequestMatchers("/login", "/comment","/css/**",
                                "/js/**", "/images/**", "/main.css","/extend-session", "/session-info")
                );


        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/css/**", "/js/**", "/images/**","/main.css").permitAll()
                .requestMatchers("/admin/access-denied").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/login", "/register").permitAll()
                .anyRequest().authenticated()
        );

        http.formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/list", true)
                .successHandler(new LoginSuccessHandler())
        );

        http.userDetailsService(userDetailsService);

        http.exceptionHandling(exception -> exception
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.sendRedirect("/access-denied");
                })
        );



        return http.build();
    }
}


//FilterChain -> 모든 유저의 요청과 서버의 응답 사이에 자동으로 실행해주고 싶은 코드 담는곳
// csrf -> 공부좀 더 많이 해야됌