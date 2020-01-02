package com.example.helloworld.config

import com.example.helloworld.repository.GreetingRepository
import com.example.helloworld.service.GreetingGrpcService
import com.linecorp.armeria.server.docs.DocService
import com.linecorp.armeria.server.grpc.GrpcService
import com.linecorp.armeria.server.logging.AccessLogWriter
import com.linecorp.armeria.server.logging.LoggingService
import com.linecorp.armeria.spring.ArmeriaServerConfigurator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ArmeriaConfiguration(
        private val repository: GreetingRepository
) {

    @Bean
    fun armeriaServerConfigurator(): ArmeriaServerConfigurator {
        return ArmeriaServerConfigurator {
            it.serviceUnder("/docs", DocService())
            it.decorator(LoggingService.newDecorator())
            it.accessLogWriter(AccessLogWriter.combined(), false)
            it.service(GrpcService.builder()
                    .addService(GreetingGrpcService(repository))
                    .build())
        }
    }
}