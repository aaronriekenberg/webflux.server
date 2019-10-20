package org.aaron.webflux.server.config

import mu.KotlinLogging
import org.aaron.webflux.server.model.StaticPathConfiguration
import org.springframework.boot.context.properties.ConfigurationProperties
import javax.annotation.PostConstruct

private val logger = KotlinLogging.logger {}

@ConfigurationProperties(prefix = "static-paths")
data class StaticPathsConfig(
        var paths: List<StaticPathConfiguration> = listOf()) {

    @PostConstruct
    fun postConstruct() {
        logger.info { "$this" }
    }
}