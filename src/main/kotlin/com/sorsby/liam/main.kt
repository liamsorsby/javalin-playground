package com.sorsby.liam

import com.sorsby.liam.config.ApplicationConfig

fun main() {
    ApplicationConfigurator()
        .createApp(ApplicationConfig.prometheusPort)
        .start(ApplicationConfig.applicationPort)
}
