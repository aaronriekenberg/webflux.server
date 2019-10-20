package org.aaron.webflux.server.config

import mu.KotlinLogging
import org.aaron.webflux.server.model.Command
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import javax.annotation.PostConstruct

private val logger = KotlinLogging.logger {}

@ConstructorBinding
@ConfigurationProperties(prefix = "command-config")
data class CommandConfig(
        val commands: List<Command> = listOf()) {

    @PostConstruct
    fun postConstruct() {
        logger.info { "$this" }
    }
}