package org.aaron.webflux.server.config

import mu.KotlinLogging
import org.aaron.webflux.server.model.ProxyConfiguration
import org.springframework.boot.context.properties.ConfigurationProperties
import javax.annotation.PostConstruct

private val logger = KotlinLogging.logger {}

@ConfigurationProperties(prefix = "proxy-config")
data class ProxyConfig(
        var proxies: List<ProxyConfiguration> = listOf()) {

    @PostConstruct
    fun postConstruct() {
        logger.info { "$this" }
    }
}