package com.book.manager.presentation.config

import org.springframework.context.annotation.Bean
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession

@EnableRedisHttpSession
class HttpSessionConfig {
    @Bean
    // spring-session-data-redisでDIコンテナとして利用
    fun connectionFactory(): JedisConnectionFactory {
        return JedisConnectionFactory()
    }
}