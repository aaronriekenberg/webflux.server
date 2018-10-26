package org.aaron.webflux.server.config

import mu.KLogging
import org.aaron.webflux.server.model.MutableCommand
import org.springframework.boot.context.properties.ConfigurationProperties
import javax.annotation.PostConstruct

@ConfigurationProperties(prefix = "command-config")
data class CommandConfig(
        var commands: MutableList<MutableCommand> = mutableListOf(),
        var elasticThreadTTLSeconds: Int = 30) {

    companion object : KLogging()

    @PostConstruct
    fun postConstruct() {
        logger.info { "$this" }
    }
}