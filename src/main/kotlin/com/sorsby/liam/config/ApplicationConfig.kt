package com.sorsby.liam.config

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory

object ApplicationConfig {
    private val conf: Config = ConfigFactory.load().resolve()

    val prometheusPort = conf.getInt("app.prometheus.port")
    val applicationPort = conf.getInt("app.port")
}
