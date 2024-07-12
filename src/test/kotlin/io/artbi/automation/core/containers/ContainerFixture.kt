package io.artbi.automation.core.containers

import mu.KLogging
import org.junit.jupiter.api.BeforeAll

open class ContainerFixture {
    companion object : KLogging() {
        @BeforeAll
        @JvmStatic
        fun setup() {
            // Start all containers
            Containers.mysqlContainer.start()
            Containers.timecoderApiContainer.start()
            Containers.timecoderPatronsContainer.start()
            Containers.timecoderGatewayContainer.start()
            Containers.uiContainer.start()
        }
    }
}