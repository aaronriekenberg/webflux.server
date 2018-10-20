package org.aaron.webflux.server.config

import mu.KLogging
import org.aaron.webflux.server.model.MutableStaticPath
import org.springframework.boot.context.properties.ConfigurationProperties
import javax.annotation.PostConstruct

@ConfigurationProperties(prefix = "static-paths")
data class StaticPathsConfig(
        var paths: MutableList<MutableStaticPath> = mutableListOf()) {

    companion object : KLogging()

    @PostConstruct
    fun postConstruct() {
        logger.info { "$this" }
    }
}