package org.aaron.webflux.server.model

import java.time.OffsetDateTime

data class MutableCommand(
        var id: String? = null,
        var description: String? = null,
        var command: String? = null,
        var arguments: MutableList<String>? = null) {

    fun toCommand(): Command {
        val arguments: List<String> = this.arguments ?: emptyList()
        val commandAndArguments = (listOf(this.command!!) + arguments).joinToString(separator = " ")

        return Command(
                id = this.id!!,
                description = this.description!!,
                command = this.command!!,
                arguments = arguments,
                commandAndArguments = commandAndArguments
        )
    }

}

class Command(
        val id: String,
        val description: String,
        val command: String,
        val arguments: List<String>,
        val commandAndArguments: String)

data class CommandAPIResult(
        val command: Command,
        val now: OffsetDateTime,
        val output: String,
        val exitValue: Int
)