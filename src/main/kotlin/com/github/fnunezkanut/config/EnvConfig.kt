/**
 * purpose of this component is to manage env parameter variables
 * to enter the application in a matter which could be mockable in tests
 */

package com.github.fnunezkanut.config


import com.github.fnunezkanut.util.masked
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
data class EnvConfig(
    @Value("\${jdbc.url}") val jdbcUrl: String,
    @Value("\${jdbc.user}") val jdbcUser: String,
    @Value("\${jdbc.pass}") val jdbcPass: String
) {


    override fun toString(): String {
        return """
jdbc.url            : $jdbcUrl
jdbc.user           : $jdbcUser
jdbc.pass           : ${jdbcPass.masked(1)}
""".trimIndent().trim()
    }
}