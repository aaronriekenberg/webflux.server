package org.aaron.webflux.server.model

import java.time.OffsetDateTime

data class CommandConfiguration(
        val id: String,
        val description: String,
        val command: String,
        val arguments: List<String> = listOf()) {

    fun toCommand(): Command =
            Command(id = this.id,
                    description = this.description,
                    command = this.command,
                    arguments = this.arguments,
                    commandAndArguments = (listOf(command) + arguments).joinToString(separator = " "))

}

data class Command(
        val id: String,
        val description: String,
        val command: String,
        val arguments: List<String>,
        val commandAndArguments: String
)

data class CommandAPIResult(
        val command: Command,
        val now: OffsetDateTime,
        val output: String,
        val exitValue: Int
)