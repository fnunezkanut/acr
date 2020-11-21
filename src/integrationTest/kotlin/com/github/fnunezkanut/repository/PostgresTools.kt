package com.github.fnunezkanut.repository

import com.zaxxer.hikari.HikariDataSource
import mu.KotlinLogging
import org.flywaydb.core.Flyway
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.testcontainers.containers.PostgreSQLContainer

//boilerplate needed for testcontainers
class KPgContainer(imageName: String?) : PostgreSQLContainer<KPgContainer>(imageName)

class PostgresTool {

    private val logger = KotlinLogging.logger {}

    //get test variables from env if present
    private val testJdbcUrl = System.getenv("TEST_JDBC_URL") ?: "jdbc:postgresql://localhost:5432/postgres"
    private val testJdbcUsername = System.getenv("TEST_JDBC_USER") ?: "acr"
    private val testJdbcPassword = System.getenv("TEST_JDBC_PASS") ?: "acr"

    //testcontainer and jdbc dependancies
    private val container: PostgreSQLContainer<*>?
    private val dataSource: HikariDataSource
    val jdbcTemplate: NamedParameterJdbcTemplate

    //setup boilerplate
    init {

        logger.info { "using testcontainers" }

        container = KPgContainer("postgres:12-alpine") //keep same as rds
            .withDatabaseName(testJdbcUrl.split("/").last())
            .withUsername(testJdbcUsername)
            .withPassword(testJdbcPassword)

        container.start().apply {
            val jdbcUrl = container.getJdbcUrl()
            dataSource = DataSourceBuilder.create()
                .url(jdbcUrl)
                .username(testJdbcUsername)
                .password(testJdbcPassword)
                .type(HikariDataSource::class.java)
                .build()
            jdbcTemplate = NamedParameterJdbcTemplate(dataSource)

            //run flyway migration of scripts in src/main/resources/db/migration setting up pristine db in a test container
            try {
                val flyway = Flyway.configure()
                    .dataSource(jdbcUrl, testJdbcUsername, testJdbcPassword)
                    .load()
                flyway.migrate()
            } catch (e: Exception) {
                logger.error("unable to perform flyway migration", e)
            }
        }

    }

    fun stop() {
        dataSource.close()
        container?.stop()
    }
}