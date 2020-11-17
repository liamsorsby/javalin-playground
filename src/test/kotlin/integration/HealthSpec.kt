package integration

import kotlin.random.Random
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.sorsby.liam.ApplicationConfigurator
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object HealthSpec : Spek({
    val port = Random.nextInt(10000, 35000)
    val promPort = Random.nextInt(10000, 35000)

    val app = ApplicationConfigurator().createApp(promPort)
    val localUrl = "http://localhost:$port"

    beforeGroup {
        app.start(port)
    }

    afterGroup {
        app.stop()
    }

    describe("GET /alive") {
        describe("when sending a get request") {
            val response by memoized {
                khttp.get("$localUrl/alive")
            }

            it("returns a 200 status code") {
                assertThat(response.statusCode, equalTo(200))
            }
        }
    }

    describe("GET /ready") {
        describe("when sending a get request") {
            val response by memoized {
                khttp.get("$localUrl/ready")
            }

            it("returns a 200 status code") {
                assertThat(response.statusCode, equalTo(200))
            }
        }
    }
})
