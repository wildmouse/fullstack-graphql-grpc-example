package com.example.helloworld.config

import com.example.helloworld.grpc.greeting.GetGreetingRequest
import com.example.helloworld.grpc.greeting.GetGreetingsRequest
import com.example.helloworld.grpc.greeting.GreetingServiceGrpc
import com.example.helloworld.grpc.greeting.SaveGreetingRequest
import com.example.helloworld.repository.GreetingRepository
import com.example.helloworld.service.GreetingGrpcService
import com.linecorp.armeria.common.grpc.GrpcSerializationFormats
import com.linecorp.armeria.server.docs.DocServiceBuilder
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
            it.serviceUnder("/docs", DocServiceBuilder()
                    .exampleRequestForMethod(
                            GreetingServiceGrpc.SERVICE_NAME,
                            "GetGreeting",
                            GetGreetingRequest.newBuilder().setId(1).build()
                    )
                    .exampleRequestForMethod(
                            GreetingServiceGrpc.SERVICE_NAME,
                            "GetGreetings",
                            GetGreetingsRequest.newBuilder().build()
                    )
                    .exampleRequestForMethod(
                            GreetingServiceGrpc.SERVICE_NAME,
                            "SaveGreeting",
                            SaveGreetingRequest.newBuilder().setMessage("Hello, World!").build()
                    )
                    .build()
            )
            it.decorator(LoggingService.newDecorator())
            it.accessLogWriter(AccessLogWriter.combined(), false)
            it.service(GrpcService.builder()
                    .addService(GreetingGrpcService(repository))
                    .supportedSerializationFormats(GrpcSerializationFormats.values())
                    .enableUnframedRequests(true)
                    .build())
        }
    }
}