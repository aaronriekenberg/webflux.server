package org.aaron.webflux.server.model

data class CommandResult(
        val command: Command,
        val output: String,
        val exitValue: Int
)