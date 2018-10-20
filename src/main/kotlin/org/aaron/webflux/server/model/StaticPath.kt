package org.aaron.webflux.server.model

data class MutableStaticPath(
        var path: String? = null) {

    fun toStaticPath(): StaticPath {
        return StaticPath(
                path = path!!
        )
    }
}

data class StaticPath(
        val path: String
)