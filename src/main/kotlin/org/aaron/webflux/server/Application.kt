package org.aaron.webflux.server

import org.aaron.webflux.server.config.CommandConfig
import org.aaron.webflux.server.config.MainPageConfig
import org.aaron.webflux.server.config.ProxyConfig
import org.aaron.webflux.server.config.StaticPathsConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(CommandConfig::class, MainPageConfig::class, ProxyConfig::class, StaticPathsConfig::class)
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}