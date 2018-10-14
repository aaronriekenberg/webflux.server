package org.aaron.webflux.server.model

import java.time.OffsetDateTime

data class CommandResult(
        val command: Command,
        val startTime: OffsetDateTime,
        val output: String,
        val exitValue: Int
)