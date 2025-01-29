package com.book.manager.presentation.config

import com.book.manager.application.service.AuthenticationService
import com.book.manager.application.service.BookManagerUserDetailsService
import com.book.manager.domain.enum.RoleType
import com.book.manager.presentation.handler.BookManagerAccessDeniedHandler
import com.book.manager.presentation.handler.BookManagerAuthenticationEntryPoint
import com.book.manager.presentation.handler.BookManagerAuthenticationFailureHandler
import com.book.manager.presentation.handler.BookManagerAuthenticationSuccessHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
class SecurityConfig(private val authenticationService: AuthenticationService) {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        val authManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder::class.java)

        authManagerBuilder
            // 認証処理実行クラス設定
            .userDetailsService(BookManagerUserDetailsService(authenticationService))
            // パスワード暗号化アルゴリズム: BCrypt
            .passwordEncoder(BCryptPasswordEncoder())
        val authManager = authManagerBuilder.build()

        http.authenticationManager(authManager)
            .authorizeHttpRequests {
            // 認可設定
            it.requestMatchers("/login").permitAll()
                .requestMatchers("/admin/**").hasAuthority(RoleType.ADMIN.toString())
                .anyRequest().authenticated()
        }.csrf {
            it.disable()
        }.formLogin {
            // 認証設定（フォームログイン）
            it.loginProcessingUrl("/login")
                .usernameParameter("email")
                .passwordParameter("pass")
                // 認証認可字の各種ハンドラ設定
                .successHandler(BookManagerAuthenticationSuccessHandler())
                .failureHandler(BookManagerAuthenticationFailureHandler())
        }.exceptionHandling {
            // 未認証
            it.authenticationEntryPoint(BookManagerAuthenticationEntryPoint())
                // 認証失敗
                .accessDeniedHandler(BookManagerAccessDeniedHandler())
        }.cors {
            it.configurationSource(corsConfigurationSource())
        }
        return http.build()
    }

    // CORS設定
    private fun corsConfigurationSource(): CorsConfigurationSource {
        val corsConfiguration = CorsConfiguration()
        corsConfiguration.addAllowedMethod(CorsConfiguration.ALL)
        corsConfiguration.addAllowedHeader(CorsConfiguration.ALL)
        corsConfiguration.addAllowedOrigin("http://localhost:8081")
        corsConfiguration.allowCredentials = true

        val corsConfigurationSource = UrlBasedCorsConfigurationSource()
        corsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration)

        return corsConfigurationSource
    }
}