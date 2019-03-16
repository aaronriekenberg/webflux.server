package org.aaron.webflux.server.util

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.reactive.result.view.Rendering
import reactor.core.publisher.Mono

fun <T> Mono<T>.mapToOKResponseEntity(): Mono<ResponseEntity<T>> {
    return this.map { result -> ResponseEntity.ok(result) }
}

fun <T> Mono<ResponseEntity<T>>.notFoundResponseEntityIfEmpty(): Mono<ResponseEntity<T>> {
    return this.switchIfEmpty(Mono.fromCallable { ResponseEntity<T>(HttpStatus.NOT_FOUND) })
}

fun <T> Mono<T>.okOrNotFoundResponseEntityIfEmpty(): Mono<ResponseEntity<T>> {
    return this.mapToOKResponseEntity().notFoundResponseEntityIfEmpty()
}

fun Mono<Rendering>.notFoundRenderingViewIfEmpty(): Mono<Rendering> {
    return this.defaultIfEmpty(
            Rendering.view("error/404").apply {
                status(HttpStatus.NOT_FOUND)
            }.build()
    )
}