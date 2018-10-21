package org.aaron.webflux.server.web

import mu.KLogging
import org.aaron.webflux.server.config.MainPageConfig
import org.aaron.webflux.server.service.CommandService
import org.aaron.webflux.server.service.ProxyService
import org.aaron.webflux.server.service.StaticPathsService
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.reactive.result.view.Rendering

@Controller
class IndexPage(
        private val commandService: CommandService,
        private val mainPageConfig: MainPageConfig,
        private val proxyService: ProxyService,
        private val staticPathsService: StaticPathsService) {

    companion object : KLogging()

    @GetMapping("/")
    fun index(): Rendering {
        return Rendering.view("index")
                .modelAttribute("mainPageConfig", mainPageConfig)
                .modelAttribute("commands", commandService.getCommands())
                .modelAttribute("proxies", proxyService.getProxies())
                .modelAttribute("staticPaths", staticPathsService.getStaticPaths())
                .status(HttpStatus.OK)
                .build()
    }
}