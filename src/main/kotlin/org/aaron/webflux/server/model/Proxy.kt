package org.aaron.webflux.server.model

data class MutableProxy(
        var id: String? = null,
        var description: String? = null,
        var url: String? = null) {

    fun toProxy(): Proxy {
        return Proxy(
                id = this.id!!,
                description = this.description!!,
                url = this.url!!
        )
    }

}

data class Proxy(
        val id: String,
        val description: String,
        val url: String
)