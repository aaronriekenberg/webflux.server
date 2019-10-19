package org.aaron.webflux.server.api

import mu.KLogging
import mu.KotlinLogging
import org.aaron.webflux.server.model.ProxyAPIResult
import org.aaron.webflux.server.service.ProxyService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

private val logger = KotlinLogging.logger {}

@RestController
@RequestMapping("/api/proxies")
class ProxyController(
        private val proxyService: ProxyService) {

    companion object : KLogging()

    @GetMapping("/{id}")
    suspend fun makeRequest(@PathVariable(required = true) id: String): ResponseEntity<ProxyAPIResult> {
        logger.debug { "makeRequest id = $id" }
        val proxyResult = proxyService.makeRequest(id)
        return if (proxyResult == null) {
            ResponseEntity(HttpStatus.NOT_FOUND)
        } else {
            ResponseEntity.ok(proxyResult)
        }
    }
}