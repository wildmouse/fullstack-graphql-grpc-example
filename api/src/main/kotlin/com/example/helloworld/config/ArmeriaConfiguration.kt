package com.example.helloworld.config

import com.example.helloworld.service.GreetingService
import com.example.helloworld.service.HelloServiceImpl
import com.linecorp.armeria.server.docs.DocService
import com.linecorp.armeria.server.grpc.GrpcService
import com.linecorp.armeria.server.logging.AccessLogWriter
import com.linecorp.armeria.server.logging.LoggingService
import com.linecorp.armeria.spring.ArmeriaServerConfigurator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ArmeriaConfiguration {

    @Bean
    fun armeriaServerConfigurator(): ArmeriaServerConfigurator {
        return ArmeriaServerConfigurator {
            it.serviceUnder("/docs", DocService())
            it.decorator(LoggingService.newDecorator())
            it.accessLogWriter(AccessLogWriter.combined(), false)
            it.service(GrpcService.builder()
                    .addService(HelloServiceImpl())
                    .addService(GreetingService())
                    .build())
        }
    }
}