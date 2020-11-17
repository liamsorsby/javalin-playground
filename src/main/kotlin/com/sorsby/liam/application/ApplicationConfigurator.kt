package com.sorsby.liam

import com.sorsby.liam.config.ApplicationConfig
import com.sorsby.liam.controllers.HealthController
import com.sorsby.liam.controllers.HelloController
import com.sorsby.liam.exception.ApplicationException
import com.sorsby.liam.metrics.StatisticsHandlerCollector
import com.sorsby.liam.models.ErrorResponse
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.http.BadRequestResponse
import io.javalin.http.HttpResponseException
import io.javalin.plugin.openapi.OpenApiOptions
import io.javalin.plugin.openapi.OpenApiPlugin
import io.javalin.plugin.openapi.dsl.OpenApiDocumentation
import io.javalin.plugin.openapi.ui.SwaggerOptions
import io.prometheus.client.exporter.HTTPServer
import io.prometheus.client.hotspot.DefaultExports
import io.swagger.v3.oas.models.info.Info
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.handler.StatisticsHandler

class ApplicationConfigurator {
    fun createApp(prometheusPort: Int): Javalin {
        val statisticsHandler = StatisticsHandler().also {
            initializePrometheus(it, prometheusPort)
        }

        val app: Javalin = Javalin.create {
            it.server {
                Server().apply {
                    handler = statisticsHandler
                }
            }
            it.registerPlugin(setupDocs())
            it.defaultContentType = "application/json"
            it.asyncRequestTimeout = ApplicationConfig.applicationAsyncTimout
            it.showJavalinBanner = false
            it.enableCorsForAllOrigins()
        }

        this.setupRoutes(app)
        this.setupExceptionHandlers(app)

        return app
    }

    private fun setupDocs() = OpenApiPlugin(
        OpenApiOptions(
            Info().apply {
                version("1.0.0")
                description("Javalin Playground")
            }
        ).apply {
            path("/swagger-docs")
            swagger(SwaggerOptions("/swagger-ui"))
            defaultDocumentation { doc: OpenApiDocumentation ->
                doc.json("400", ErrorResponse::class.java)
                doc.json("404", ErrorResponse::class.java)
                doc.json("500", ErrorResponse::class.java)
            }
        }
    )

    private fun setupExceptionHandlers(app: Javalin) {
        app.exception(ApplicationException::class.java) { e, ctx ->
            ctx.json(ErrorResponse(e.message.toString())).status(e.statusCode)
        }.exception(BadRequestResponse::class.java) { e, ctx ->
            ctx.json(ErrorResponse(e.message.toString())).status(400)
        }.exception(HttpResponseException::class.java) { e, ctx ->
            ctx.json(ErrorResponse(e.message.toString())).status(e.status)
        }
    }

    private fun initializePrometheus(statisticsHandler: StatisticsHandler, prometheusPort: Int) {
        StatisticsHandlerCollector.initialize(statisticsHandler)
        HTTPServer(prometheusPort)
        DefaultExports.initialize()
    }

    private fun setupRoutes(app: Javalin) {
        app.routes {
            path("/api/v1/hello") {
                get(HelloController::getHello)
            }
            path("/alive") {
                get(HealthController::alive)
            }
            path("/ready") {
                get(HealthController::ready)
            }
        }
    }
}
