package org.aaron.webflux.server.config

import mu.KLogging
import org.aaron.webflux.server.model.MutableProxy
import org.springframework.boot.context.properties.ConfigurationProperties
import javax.annotation.PostConstruct

@ConfigurationProperties(prefix = "proxy-config")
data class ProxyConfig(
        var proxies: MutableList<MutableProxy> = mutableListOf()) {

    companion object : KLogging()

    @PostConstruct
    fun postConstruct() {
        logger.info { "$this" }
    }
}