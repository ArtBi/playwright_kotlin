package io.artbi.automation.core.containers

import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.containers.output.Slf4jLogConsumer
import org.testcontainers.utility.DockerImageName
import org.testcontainers.containers.wait.strategy.Wait

object Containers {
    // MySQL container setup
    val mysqlContainer =  MySQLContainer<Nothing>(DockerImageName.parse("mysql").withTag("5.6")).apply {
        withLogConsumer(Slf4jLogConsumer(ContainerFixture.logger))
        withEnv("MYSQL_ROOT_PASSWORD","admin")
        withEnv("MYSQL_DATABASE","timecoder")
        withExposedPorts(3306)
        withCreateContainerCmdModifier{ it.withPlatform("linux/x86_64")}
        withCreateContainerCmdModifier { it.withName("mysql") }
        withFileSystemBind("./mycustom.cnf","/etc/mysql/conf.d/custom.cnf")
    }

    // Custom API container setup
    val timecoderApiContainer = GenericContainer<Nothing>(DockerImageName.parse("spirogov/timecoder:latest")).apply {
        withExposedPorts(8080) // Expose the port the API listens on
        withEnv("SPRING_PROFILES_ACTIVE","prod")
        withEnv("USER_NAME","root")
        withEnv("PASSWORD","admin")
        dependsOn(mysqlContainer) // Ensure API starts after MySQL
        withCreateContainerCmdModifier{ it.withPlatform("linux/x86_64")}
        withCreateContainerCmdModifier { it.withName("timecoder") }
        withLogConsumer(Slf4jLogConsumer(ContainerFixture.logger))
        waitingFor(Wait.forListeningPort()) // Adjust the path as needed
    }

    // Additional containers can be added here following the same pattern
    val timecoderPatronsContainer = GenericContainer<Nothing>(DockerImageName.parse("spirogov/timecoder-patrons:latest")).apply {
        withExposedPorts(8081) // Example port
        withEnv("accessToken","")
        withEnv("campaign","")
        waitingFor(Wait.forListeningPort()) // Waits for the exposed port to be available
        withCreateContainerCmdModifier{ it.withPlatform("linux/x86_64")}
        withCreateContainerCmdModifier { it.withName("timecoder-patrons") }
        withLogConsumer(Slf4jLogConsumer(ContainerFixture.logger))
    }

    val timecoderGatewayContainer = GenericContainer<Nothing>(DockerImageName.parse("spirogov/timecoder-gateway:latest")).apply {
        withExposedPorts(8086) // Example port
        dependsOn(timecoderApiContainer) // Dependency to ensure order of startup
        waitingFor(Wait.forListeningPort()) // Simple wait strategy
        withEnv("SPRING_PROFILES_ACTIVE","prod")
        withEnv("USER_NAME","root")
        withEnv("PASSWORD","admin")
        withCreateContainerCmdModifier{ it.withPlatform("linux/x86_64")}
        withCreateContainerCmdModifier { it.withName("timecoder-gateway") }
        withLogConsumer(Slf4jLogConsumer(ContainerFixture.logger))
    }

    val uiContainer = GenericContainer<Nothing>(DockerImageName.parse("spirogov/timecoder-ui:latest")).apply {
        withExposedPorts(80, 443) // Expose HTTP and HTTPS ports
        dependsOn(timecoderGatewayContainer) // UI should start after the gateway
        waitingFor(Wait.forListeningPort()) // Wait for the web server to be up
        withCreateContainerCmdModifier{ it.withPlatform("linux/x86_64")}
        withLogConsumer(Slf4jLogConsumer(ContainerFixture.logger))
        withFileSystemBind("./local/nginx.conf","/etc/nginx/conf.d/default.conf")
    }
}