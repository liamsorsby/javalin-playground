package com.sorsby.liam

import com.sorsby.liam.config.ApplicationConfig

fun main() {
    ApplicationConfigurator()
        .createApp()
        .start(ApplicationConfig.applicationPort)
}
