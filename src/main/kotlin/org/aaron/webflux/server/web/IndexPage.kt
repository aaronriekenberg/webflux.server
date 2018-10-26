package org.aaron.webflux.server.web

import mu.KLogging
import org.aaron.webflux.server.config.MainPageConfig
import org.aaron.webflux.server.service.CommandService
import org.aaron.webflux.server.service.ProxyService
import org.aaron.webflux.server.service.StaticPathsService
import org.springframework.http.CacheControl
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.reactive.result.view.Rendering
import java.time.OffsetDateTime
import java.util.concurrent.TimeUnit

@Controller
class IndexPage(
        private val commandService: CommandService,
        private val mainPageConfig: MainPageConfig,
        private val proxyService: ProxyService,
        private val staticPathsService: StaticPathsService) {

    companion object : KLogging()

    private val startTime = OffsetDateTime.now()

    private val startTimeString = startTime.toString()

    @GetMapping("/")
    fun index(): Rendering {
        val headers = HttpHeaders().apply {
            setCacheControl(CacheControl.maxAge(1, TimeUnit.MINUTES))
            lastModified = startTime.toInstant().toEpochMilli()
        }
        return Rendering.view("index")
                .modelAttribute("mainPageConfig", mainPageConfig)
                .modelAttribute("commands", commandService.getCommands())
                .modelAttribute("proxies", proxyService.getProxies())
                .modelAttribute("staticPaths", staticPathsService.getStaticPaths())
                .modelAttribute("startTime", startTimeString)
                .headers(headers)
                .status(HttpStatus.OK)
                .build()
    }
}