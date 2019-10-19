package org.aaron.webflux.server.config

import mu.KotlinLogging
import org.aaron.webflux.server.model.MutableCommand
import org.springframework.boot.context.properties.ConfigurationProperties
import javax.annotation.PostConstruct

private val logger = KotlinLogging.logger {}

@ConfigurationProperties(prefix = "command-config")
data class CommandConfig(
        var commands: MutableList<MutableCommand> = mutableListOf()) {

    @PostConstruct
    fun postConstruct() {
        logger.info { "$this" }
    }
}