package org.aaron.webflux.server

import org.aaron.webflux.server.config.CommandConfig
import org.aaron.webflux.server.config.ProxyConfig
import org.aaron.webflux.server.config.StaticPathsConfig
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties

@SpringBootApplication
@EnableConfigurationProperties(CommandConfig::class, ProxyConfig::class, StaticPathsConfig::class)
class Application {

}

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}