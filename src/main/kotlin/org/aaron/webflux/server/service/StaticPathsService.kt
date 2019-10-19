package org.aaron.webflux.server.service

import mu.KotlinLogging
import org.aaron.webflux.server.config.StaticPathsConfig
import org.aaron.webflux.server.model.MutableStaticPath
import org.aaron.webflux.server.model.StaticPath
import org.springframework.stereotype.Service

private val logger = KotlinLogging.logger {}

@Service
class StaticPathsService(
        staticPathsConfig: StaticPathsConfig) {

    private val staticPaths: List<StaticPath> = staticPathsConfig.paths
            .asSequence()
            .map(MutableStaticPath::toStaticPath)
            .toList()

    fun getStaticPaths(): List<StaticPath> = staticPaths

}