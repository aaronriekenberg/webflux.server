package org.aaron.webflux.server.config

import mu.KLogging
import org.springframework.boot.context.properties.ConfigurationProperties
import javax.annotation.PostConstruct

@ConfigurationProperties(prefix = "main-page")
data class MainPageConfig(
        var title: String?) {

    companion object : KLogging()

    @PostConstruct
    fun postConstruct() {
        logger.info { "$this" }
    }
}