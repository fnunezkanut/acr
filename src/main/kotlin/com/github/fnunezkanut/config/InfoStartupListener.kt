package com.github.fnunezkanut.config

import mu.KotlinLogging
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Component


//prints out passed in params on startup
@Component
class InfoStartupListener(
    private val config: EnvConfig
) : ApplicationListener<ApplicationReadyEvent> {
    private val logger = KotlinLogging.logger {}

    override fun onApplicationEvent(event: ApplicationReadyEvent) {
        logger.info { "incoming parameters: \n$config" }
    }
}