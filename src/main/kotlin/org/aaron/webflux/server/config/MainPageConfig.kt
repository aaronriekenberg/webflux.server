package org.aaron.webflux.server.config

import mu.KotlinLogging
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import javax.annotation.PostConstruct

private val logger = KotlinLogging.logger {}

@ConstructorBinding
@ConfigurationProperties(prefix = "main-page")
data class MainPageConfig(
        val title: String) {

    @PostConstruct
    fun postConstruct() {
        logger.info { "$this" }
    }
}