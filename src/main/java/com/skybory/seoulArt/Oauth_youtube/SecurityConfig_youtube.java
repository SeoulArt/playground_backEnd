//package com.skybory.ticketing.security;
//
//import java.io.PrintWriter;
//
//import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.web.AuthenticationEntryPoint;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.access.AccessDeniedHandler;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.skybory.ticketing.domain.member.Role;
//
//import lombok.Getter;
//import lombok.RequiredArgsConstructor;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//
//        http
//                .csrf((csrfConfig) ->
//                        csrfConfig.disable()
//                ) // 1번
//                .headers((headerConfig) ->
//                        headerConfig.frameOptions(frameOptionsConfig ->
//                                frameOptionsConfig.disable()
//                        )
//                )// 2번
//                .authorizeHttpRequests((authorizeRequests) ->
//                        authorizeRequests
//                                .requestMatchers(PathRequest.toH2Console()).permitAll()
//                                .requestMatchers("/", "/login/**").permitAll()
//                                .requestMatchers("/posts/**", "/api/v1/posts/**").hasRole(Role.USER.name())
//                                .requestMatchers("/admins/**", "/api/v1/admins/**").hasRole(Role.ADMIN.name())
//                                .anyRequest().authenticated()
//                )// 3번
//                .exceptionHandling((exceptionConfig) ->
//                        exceptionConfig.authenticationEntryPoint(unauthorizedEntryPoint).accessDeniedHandler(accessDeniedHandler)
//                ); // 401 403 관련 예외처리
//
//        return http.build();
//    }
//
//    private final AuthenticationEntryPoint unauthorizedEntryPoint =
//            (request, response, authException) -> {
//                ErrorResponse fail = new ErrorResponse(HttpStatus.UNAUTHORIZED, "Spring security unauthorized...");
//                response.setStatus(HttpStatus.UNAUTHORIZED.value());
//                String json = new ObjectMapper().writeValueAsString(fail);
//                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//                PrintWriter writer = response.getWriter();
//                writer.write(json);
//                writer.flush();
//            };
//
//    private final AccessDeniedHandler accessDeniedHandler =
//            (request, response, accessDeniedException) -> {
//                ErrorResponse fail = new ErrorResponse(HttpStatus.FORBIDDEN, "Spring security forbidden...");
//                response.setStatus(HttpStatus.FORBIDDEN.value());
//                String json = new ObjectMapper().writeValueAsString(fail);
//                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//                PrintWriter writer = response.getWriter();
//                writer.write(json);
//                writer.flush();
//            };
//
//  	@Getter
//    @RequiredArgsConstructor
//    public class ErrorResponse {
//
//        private final HttpStatus status;
//        private final String message;
//    }
//}
//////import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
//////import org.springframework.context.annotation.Bean;
//////import org.springframework.context.annotation.Configuration;
//////import org.springframework.security.config.Customizer;
//////import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//////import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
//////import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//////import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
//////import org.springframework.security.web.SecurityFilterChain;
//////import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//////
//////@Configuration
//////public class SecurityConfig {
//////
//////    @Bean
//////    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//////
//////        http
//////            .csrf(AbstractHttpConfigurer::disable)
//////            .formLogin(Customizer.withDefaults())
//////            .authorizeHttpRequests(authorizeRequest ->
//////                    authorizeRequest
//////                            .requestMatchers(
//////                                    AntPathRequestMatcher.antMatcher("/auth/**")
//////                            ).authenticated()
//////                            .requestMatchers(
//////                                    AntPathRequestMatcher.antMatcher("/h2-console/**")
//////                            ).permitAll()
//////            )
//////            .headers(
//////                    headersConfigurer ->
//////                            headersConfigurer
//////                                    .frameOptions(
//////                                            HeadersConfigurer.FrameOptionsConfig::sameOrigin
//////                                    )
//////            );
//////
//////        return http.build();
//////    }
//////
//////    @Bean
//////    public WebSecurityCustomizer webSecurityCustomizer() {
//////        // 정적 리소스 spring security 대상에서 제외
//////        return (web) ->
//////                web
//////                    .ignoring()
//////                    .requestMatchers(
//////                            PathRequest.toStaticResources().atCommonLocations()
//////                    );
//////    }
//////}
//