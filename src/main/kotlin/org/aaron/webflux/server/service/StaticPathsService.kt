package org.aaron.webflux.server.service

import mu.KotlinLogging
import org.aaron.webflux.server.config.StaticPathsConfig
import org.aaron.webflux.server.model.StaticPathConfiguration
import org.springframework.stereotype.Service

private val logger = KotlinLogging.logger {}

@Service
class StaticPathsService(
        staticPathsConfig: StaticPathsConfig) {

    private val staticPaths: List<StaticPathConfiguration> = staticPathsConfig.paths
            .toList()

    fun getStaticPaths(): List<StaticPathConfiguration> = staticPaths

}