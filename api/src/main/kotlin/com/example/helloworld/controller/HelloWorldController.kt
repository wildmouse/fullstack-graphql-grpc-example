package com.example.helloworld.controller

import com.example.helloworld.grpc.greeting.GetGreetingRequest
import com.example.helloworld.grpc.greeting.GetGreetingsRequest
import com.example.helloworld.grpc.greeting.GreetingServiceGrpc
import com.example.helloworld.grpc.greeting.SaveGreetingRequest
import com.example.helloworld.service.HelloWorldService
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
class HelloWorldController(
        private val service: HelloWorldService
) {

    companion object {
        private val channel: ManagedChannel = ManagedChannelBuilder
                .forAddress("localhost", 8080)
                .usePlaintext()
                .build()
        val client: GreetingServiceGrpc.GreetingServiceBlockingStub = GreetingServiceGrpc.newBlockingStub(channel)
    }

    @GetMapping("/hello-worlds/{id}")
    fun getHelloWorld(@PathVariable("id") id: Long): Mono<String> {
        val request = GetGreetingRequest.newBuilder().setId(id).build()
        val response = client.getGreeting(request)
        return Mono.just(response.message)
    }

    @GetMapping("/hello-worlds")
    fun getHelloWorldList(): Flux<String> {
        val request = GetGreetingsRequest.newBuilder().build()
        val response = client.getGreetings(request)
        return Flux.fromIterable(response.messagesList.toMutableList())
    }

    @PostMapping("/hello-worlds")
    fun saveHelloWorld(@RequestParam("message") message: String): Mono<Boolean> {
        val request = SaveGreetingRequest.newBuilder()
                .setMessage(message)
                .build()
        val response = client.saveGreeting(request)

        return Mono.just(response.isSaved)
    }
}
