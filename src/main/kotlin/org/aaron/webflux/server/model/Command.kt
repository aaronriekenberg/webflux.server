package org.aaron.webflux.server.model

import java.time.OffsetDateTime

data class Command(
        val id: String,
        val description: String,
        val command: String,
        val arguments: List<String> = listOf()) {

    val commandAndArguments: String by lazy(mode = LazyThreadSafetyMode.PUBLICATION) {
        (listOf(command) + arguments).joinToString(separator = " ")
    }

}

data class CommandAPIResult(
        val command: Command,
        val now: OffsetDateTime,
        val output: String,
        val exitValue: Int
)