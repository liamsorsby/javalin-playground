package integration

import kotlin.random.Random
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.sorsby.liam.ApplicationConfigurator
import io.prometheus.client.CollectorRegistry
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object SwaggerSpec : Spek({
    val port = Random.nextInt(10000, 35000)
    val promPort = Random.nextInt(10000, 35000)

    CollectorRegistry.defaultRegistry.clear()

    val app = ApplicationConfigurator().createApp(promPort)
    val localUrl = "http://localhost:$port"

    beforeGroup {
        app.start(port)
    }

    afterGroup {
        app.stop()
    }

    describe("GET /swagger-docs") {
        describe("when sending a get request") {
            val response by memoized {
                khttp.get("$localUrl/swagger-docs")
            }

            it("returns a 200 status code") {
                assertThat(response.statusCode, equalTo(200))
            }
        }
    }

    describe("GET /swagger-ui") {
        describe("when sending a get request") {
            val response by memoized {
                khttp.get("$localUrl/swagger-ui")
            }

            it("returns a 200 status code") {
                assertThat(response.statusCode, equalTo(200))
            }
        }
    }

    afterEachTest {
        CollectorRegistry.defaultRegistry.clear()
    }
})
