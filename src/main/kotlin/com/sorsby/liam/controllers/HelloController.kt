package com.sorsby.liam.controllers

import io.javalin.http.Context
import io.javalin.plugin.openapi.annotations.OpenApi
import io.javalin.plugin.openapi.annotations.OpenApiContent
import io.javalin.plugin.openapi.annotations.OpenApiResponse
import org.slf4j.LoggerFactory

object HelloController {
    private val logger = LoggerFactory.getLogger(HelloController::class.java)

    @OpenApi(
        description = "Returns hello",

        responses = [
            OpenApiResponse(
                status = "200",
                content = [OpenApiContent(from = HelloController::class, isArray = true)]
            ),
        ]
    )
    fun getHello(ctx: Context) {
        logger.info("say hello")
        ctx.status(200).json(mapOf(Pair("say", "hello")))
    }
}
