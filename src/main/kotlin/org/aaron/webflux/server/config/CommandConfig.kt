package org.aaron.webflux.server.config

import mu.KotlinLogging
import org.aaron.webflux.server.model.CommandConfiguration
import org.springframework.boot.context.properties.ConfigurationProperties
import javax.annotation.PostConstruct

private val logger = KotlinLogging.logger {}

@ConfigurationProperties(prefix = "command-config")
data class CommandConfig(
        var commands: List<CommandConfiguration> = listOf()) {

    @PostConstruct
    fun postConstruct() {
        logger.info { "$this" }
    }
}