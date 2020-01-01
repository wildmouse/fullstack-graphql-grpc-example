package com.example.helloworld.controller

import com.example.helloworld.grpc.HelloRequest
import com.example.helloworld.grpc.HelloServiceGrpc
import com.example.helloworld.grpc.greeting.GetGreetingsRequest
import com.example.helloworld.grpc.greeting.GetGreetingsResponse
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

    // TODO: this point is just an example. remove after necessary gRPC services are implemented.
    @PostMapping("/hello-world/grpc")
    // TODO: use Flux
    fun getHelloWorldGrpc(): Mono<List<String>> {
        val request = GetGreetingsRequest.newBuilder().build()
        val response = client.getGreetings(request)
        return Mono.just(response.messagesList.toMutableList())
    }

    @PostMapping("/hello-world/grpc/save")
    fun saveHelloWorldGrpc(): Mono<Boolean> {
        val request = SaveGreetingRequest.newBuilder().setMessage("Hello, World!").build()
        val response = client.saveGreeting(request)

        return Mono.just(response.isSaved)
    }

    @GetMapping("/hello-worlds/{id}")
    fun getHelloWorld(@PathVariable("id") id: Int) = Mono.just(service.getHelloWorld(id))

    @GetMapping("/hello-worlds")
    fun getHelloWorldList() = Flux.just(service.getHelloWorldList())

    @PostMapping("/hello-worlds")
    fun saveHelloWorld(@RequestParam("message") message: String) =
            Mono.just(service.insertHelloWorld(message).id > 0)
}
