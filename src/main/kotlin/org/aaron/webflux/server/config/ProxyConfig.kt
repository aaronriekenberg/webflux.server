package org.aaron.webflux.server.config

import mu.KotlinLogging
import org.aaron.webflux.server.model.MutableProxy
import org.springframework.boot.context.properties.ConfigurationProperties
import javax.annotation.PostConstruct

private val logger = KotlinLogging.logger {}

@ConfigurationProperties(prefix = "proxy-config")
data class ProxyConfig(
        var maxRetries: Int = 2,
        var retryDelayMS: Long = 100L,
        var proxies: MutableList<MutableProxy> = mutableListOf()) {

    @PostConstruct
    fun postConstruct() {
        logger.info { "$this" }
    }
}