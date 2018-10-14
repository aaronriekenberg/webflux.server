package org.aaron.webflux.server.model

data class MutableCommand(
        var id: String? = null,
        var description: String? = null,
        var command: String? = null,
        var arguments: MutableList<String>? = null) {

    fun toCommand(): Command {
        return Command(
                id = this.id!!,
                description = this.description!!,
                command = this.command!!,
                arguments = this.arguments!!
        )
    }
    
}

class Command(
        val id: String,
        val description: String,
        val command: String,
        val arguments: List<String>)