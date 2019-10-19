package org.aaron.webflux.server.config

import mu.KotlinLogging
import org.springframework.boot.context.properties.ConfigurationProperties
import javax.annotation.PostConstruct

private val logger = KotlinLogging.logger {}

@ConfigurationProperties(prefix = "main-page")
data class MainPageConfig(
        var title: String?) {

    @PostConstruct
    fun postConstruct() {
        logger.info { "$this" }
    }
}