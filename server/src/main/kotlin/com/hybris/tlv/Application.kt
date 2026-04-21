package com.hybris.tlv

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.http.content.staticResources
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.request.path
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import org.slf4j.event.Level

fun main() {
    embeddedServer(
        factory = Netty,
        port = System.getenv("PORT")?.toInt() ?: 8080,
        host = "0.0.0.0",
        module = Application::module
    ).start(wait = true)
}

fun Application.module() {
    install(plugin = CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith(prefix = "/") }
    }

    routing {
        get(path = "/") { call.respondText(text = "Server running") }

        // TODO - Add json files  to the resource folder
        staticResources(remotePath = "/data", basePackage = "static")
    }
}
