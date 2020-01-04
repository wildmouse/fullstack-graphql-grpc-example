package com.example.helloworld.service

import com.example.helloworld.entity.Greeting
import com.example.helloworld.grpc.greeting.*
import com.example.helloworld.repository.GreetingRepository
import reactor.core.publisher.Mono

class GreetingGrpcService(
        private val repository: GreetingRepository
) : ReactorGreetingServiceGrpc.GreetingServiceImplBase() {
    override fun getGreeting(request: Mono<GetGreetingRequest>): Mono<GetGreetingResponse> =
            request.map {
                val greeting = repository.findById(it.id)
                if (greeting.isPresent) {
                    GetGreetingResponse.newBuilder()
                            .setGreeting(greeting.let {
                                com.example.helloworld.grpc.greeting.Greeting
                                        .newBuilder()
                                        .setMessage(it.get().message)
                                        .setId(it.get().id)
                                        .build()
                            })
                            .build()
                } else {
                    GetGreetingResponse.getDefaultInstance()
                }
            }

    override fun getGreetings(request: Mono<GetGreetingsRequest>): Mono<GetGreetingsResponse> =
            request.map {
                val greetings = repository.findAll().map {
                    com.example.helloworld.grpc.greeting.Greeting
                            .newBuilder()
                            .setMessage(it.message)
                            .setId(it.id)
                            .build()
                }
                GetGreetingsResponse
                        .newBuilder()
                        .addAllGreetings(greetings)
                        .build()
            }

    override fun saveGreeting(request: Mono<SaveGreetingRequest>): Mono<SaveGreetingResponse> =
            request.map {
                val greeting = repository.save(Greeting(it.message)).let {
                    com.example.helloworld.grpc.greeting.Greeting
                            .newBuilder()
                            .setMessage(it.message)
                            .setId(it.id)
                            .build()
                }
                SaveGreetingResponse
                        .newBuilder()
                        .setGreeting(greeting)
                        .build()
            }
}