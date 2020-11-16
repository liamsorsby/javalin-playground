package com.sorsby.liam.controllers

import io.javalin.http.Context
import io.javalin.plugin.openapi.annotations.OpenApi

object HealthController {
    @OpenApi(
        ignore = true
    )
    fun alive(ctx: Context) {
        ctx.status(200)
    }

    @OpenApi(
        ignore = true
    )
    fun ready(ctx: Context) {
        ctx.status(200)
    }
}
