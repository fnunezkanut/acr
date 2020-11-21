package com.github.fnunezkanut

import com.github.fnunezkanut.config.EnvConfig
import com.zaxxer.hikari.HikariDataSource
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.transaction.annotation.EnableTransactionManagement
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.util.*
import javax.annotation.PostConstruct

//entry point into this springboot2 app
fun main(args: Array<String>) {
    runApplication<App>(*args)
}

@SpringBootApplication
class App {

    //ensure when running locally we are on UTC, so any timestamps in postgres are UTC
    @PostConstruct
    fun started() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
    }

    /* config beans below */

    //make the default for this app to be a json based api
    @Configuration
    @EnableWebMvc
    class WebMvcConfig : WebMvcConfigurer {
        override fun configureContentNegotiation(configurer: ContentNegotiationConfigurer?) {
            configurer!!.defaultContentType(MediaType.APPLICATION_JSON)
        }
    }


    //config for the postgres datasource using hikari connection pool
    @Configuration
    @EnableTransactionManagement(proxyTargetClass = true)
    class JdbcConfig(
        val envConfig: EnvConfig
    ) {

        @Bean
        fun pgDataSource(): HikariDataSource {

            return DataSourceBuilder.create()
                .url(envConfig.jdbcUrl)
                .username(envConfig.jdbcUser)
                .password(envConfig.jdbcPass)
                .type(HikariDataSource::class.java)
                .build()
        }

        @Bean(name = ["jdbc"])
        fun jdbc(): NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(pgDataSource())
    }
}