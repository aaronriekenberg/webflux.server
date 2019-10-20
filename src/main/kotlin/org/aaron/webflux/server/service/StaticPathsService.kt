package org.aaron.webflux.server.service

import org.aaron.webflux.server.config.StaticPathsConfig
import org.aaron.webflux.server.model.StaticPath
import org.springframework.stereotype.Service

@Service
class StaticPathsService(
        private val staticPathsConfig: StaticPathsConfig) {

    fun getStaticPaths(): List<StaticPath> = staticPathsConfig.paths

}