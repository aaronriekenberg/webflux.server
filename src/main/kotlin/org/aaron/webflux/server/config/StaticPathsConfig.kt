package org.aaron.webflux.server.config

import mu.KotlinLogging
import org.aaron.webflux.server.model.StaticPath
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import javax.annotation.PostConstruct

private val logger = KotlinLogging.logger {}

@ConstructorBinding
@ConfigurationProperties(prefix = "static-paths")
data class StaticPathsConfig(
        val paths: List<StaticPath> = listOf()) {

    @PostConstruct
    fun postConstruct() {
        logger.info { "$this" }
    }
}